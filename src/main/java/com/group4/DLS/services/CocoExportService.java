package com.group4.DLS.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group4.DLS.domain.dto.response.BoundingBoxShape;
import com.group4.DLS.domain.dto.response.PolygonShape;
import com.group4.DLS.domain.dto.response.Shape;
import com.group4.DLS.domain.entity.Annotation;
import com.group4.DLS.domain.entity.Assignment;
import com.group4.DLS.domain.entity.Dataitem;
import com.group4.DLS.domain.entity.Task;
import com.group4.DLS.helper.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CocoExportService {

    ObjectMapper mapper;

    public File export(Assignment assignment) throws Exception {

        String basePath = System.getProperty("java.io.tmpdir")
                + "/coco_" + assignment.getAssignmentName() + "_" + System.currentTimeMillis();

        File baseDir = new File(basePath);
        File imagesDir = new File(baseDir, "images");

        createDir(baseDir);
        createDir(imagesDir);

        List<Map<String, Object>> images = new ArrayList<>();
        List<Map<String, Object>> annotations = new ArrayList<>();
        Map<String, Integer> categoryMap = new HashMap<>();
        Map<String, Integer> imageMap = new HashMap<>();

        int imageId = 1;
        int annotationId = 1;

        for (Task task : assignment.getTasks()) {
            for (Annotation ann : task.getAnnotations()) {

                if (ann.getAnnotationData() == null
                        || ann.getAnnotationData().getRaw() == null) continue;

                Dataitem item = ann.getDataitem();
                String fileName = resolveFileName(item);

                // ======================
                // IMAGE (tránh duplicate)
                // ======================
                if (!imageMap.containsKey(fileName)) {

                    imageMap.put(fileName, imageId);

                    Map<String, Object> img = new HashMap<>();
                    img.put("id", imageId);
                    img.put("file_name", fileName);
                    img.put("width", item.getWidth());
                    img.put("height", item.getHeight());

                    images.add(img);

                    // download image
                    Path target = Paths.get(imagesDir.getPath(), fileName);
                    FileUtils.downloadImage(item.getUrl(), target);

                    imageId++;
                }

                int currentImageId = imageMap.get(fileName);

                // ======================
                // ANNOTATIONS
                // ======================
                for (Shape s : ann.getAnnotationData().getRaw()) {

                    if (s.getLabel() == null) continue;

                    categoryMap.putIfAbsent(s.getLabel(), categoryMap.size() + 1);
                    int categoryId = categoryMap.get(s.getLabel());

                    List<Double> bbox = null;
                    List<List<Double>> segmentation = null;

                    // ======================
                    //  POLYGON (ưu tiên)
                    // ======================
                    if (s instanceof PolygonShape polygon) {

                        if (polygon.getPoints() == null || polygon.getPoints().size() < 3) continue;

                        List<Double> seg = new ArrayList<>();

                        double minX = Double.MAX_VALUE;
                        double minY = Double.MAX_VALUE;
                        double maxX = Double.MIN_VALUE;
                        double maxY = Double.MIN_VALUE;

                        for (List<Double> p : polygon.getPoints()) {

                            if (p.size() < 2) continue;

                            double x = p.get(0);
                            double y = p.get(1);

                            seg.add(x);
                            seg.add(y);

                            minX = Math.min(minX, x);
                            minY = Math.min(minY, y);
                            maxX = Math.max(maxX, x);
                            maxY = Math.max(maxY, y);
                        }

                        // tránh polygon lỗi
                        if (seg.size() < 6) continue;

                        segmentation = List.of(seg);
                        bbox = List.of(minX, minY, maxX - minX, maxY - minY);
                    }

                    // ======================
                    //  BBOX (fallback)
                    // ======================
                    else if (s instanceof BoundingBoxShape box) {

                        if (box.getWidth() == null || box.getHeight() == null) continue;

                        bbox = List.of(
                                box.getX(),
                                box.getY(),
                                box.getWidth(),
                                box.getHeight()
                        );
                    }

                    if (bbox == null) continue;

                    Map<String, Object> anno = new HashMap<>();
                    anno.put("id", annotationId++);
                    anno.put("image_id", currentImageId);
                    anno.put("category_id", categoryId);
                    anno.put("bbox", bbox);
                    anno.put("area", bbox.get(2) * bbox.get(3));
                    anno.put("iscrowd", 0);

                    if (segmentation != null) {
                        anno.put("segmentation", segmentation);
                    }

                    annotations.add(anno);
                }
            }
        }

        // ======================
        // CATEGORIES (sort theo id)
        // ======================
        List<Map<String, Object>> categories = categoryMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(e -> {
                    Map<String, Object> cat = new HashMap<>();
                    cat.put("id", e.getValue());
                    cat.put("name", e.getKey());
                    return cat;
                })
                .toList();

        // ======================
        // FINAL JSON
        // ======================
        Map<String, Object> coco = new HashMap<>();
        coco.put("images", images);
        coco.put("annotations", annotations);
        coco.put("categories", categories);

        File jsonFile = new File(baseDir, "annotations.json");
        mapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, coco);

        // zip
        File zipFile = new File(basePath + ".zip");
        FileUtils.zipFolder(baseDir, zipFile);

        return zipFile;
    }

    private String resolveFileName(Dataitem item) {
        if (item.getFileName() != null && item.getFileName().contains(".")) {
            return item.getFileName();
        }
        String url = item.getUrl();
        return url.substring(url.lastIndexOf("/") + 1);
    }

    private void createDir(File dir) {
        if (!dir.exists() && !dir.mkdirs()) {
            throw new RuntimeException("Không tạo được thư mục: " + dir.getAbsolutePath());
        }
    }
}