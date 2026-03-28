package com.group4.DLS.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group4.DLS.domain.dto.response.AnnotationData;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CocoExportService {

    ObjectMapper mapper;

    public File export(Assignment assignment) throws Exception {

        String basePath = System.getProperty("java.io.tmpdir") + assignment.getAssignmentName()
                + "/coco_" + System.currentTimeMillis();

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

                if (ann.getAnnotationData() == null) continue;

                AnnotationData data = mapper.readValue(
                        ann.getAnnotationData(),
                        AnnotationData.class
                );

                if (data.getShapes() == null) continue;

                Dataitem item = ann.getDataitem();
                String fileName = resolveFileName(item);

                // IMAGE
                if (!imageMap.containsKey(fileName)) {

                    imageMap.put(fileName, imageId);

                    Map<String, Object> img = new HashMap<>();
                    img.put("id", imageId);
                    img.put("file_name", fileName);
                    img.put("width", item.getWidth());
                    img.put("height", item.getHeight());

                    images.add(img);

                    //  DOWNLOAD IMAGE
                    Path target = Paths.get(imagesDir.getPath(), fileName);
                    FileUtils.downloadImage(item.getUrl(), target);

                    imageId++;
                }

                int currentImageId = imageMap.get(fileName);

                //  ANNOTATION
                for (Shape s : data.getShapes()) {

                    if (s.getLabel() == null) continue;

                    categoryMap.putIfAbsent(s.getLabel(), categoryMap.size() + 1);
                    int categoryId = categoryMap.get(s.getLabel());

                    double x = s.getX();
                    double y = s.getY();
                    double w = s.getWidth();
                    double h = s.getHeight();

                    Map<String, Object> anno = new HashMap<>();
                    anno.put("id", annotationId);
                    anno.put("image_id", currentImageId);
                    anno.put("category_id", categoryId);
                    anno.put("bbox", List.of(x, y, w, h));
                    anno.put("area", w * h);
                    anno.put("iscrowd", 0);

                    annotations.add(anno);
                    annotationId++;
                }
            }
        }

        // categories
        List<Map<String, Object>> categories = new ArrayList<>();
        categoryMap.forEach((name, id) -> {
            Map<String, Object> cat = new HashMap<>();
            cat.put("id", id);
            cat.put("name", name);
            categories.add(cat);
        });

        // json
        Map<String, Object> coco = new HashMap<>();
        coco.put("images", images);
        coco.put("annotations", annotations);
        coco.put("categories", categories);

        File jsonFile = new File(baseDir, "annotations.json");
        mapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, coco);

        //  ZIP (dùng lại FileUtils)
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