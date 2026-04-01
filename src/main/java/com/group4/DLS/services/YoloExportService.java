package com.group4.DLS.services;

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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class YoloExportService {

    public File export(Assignment assignment, String mode) throws Exception {

        // mode = "DETECT" | "SEGMENT"

        String basePath = System.getProperty("java.io.tmpdir")
                + "/yolo_" + assignment.getAssignmentName() + "_" + System.currentTimeMillis();

        File baseDir = new File(basePath);

        File imagesTrainDir = new File(baseDir, "images/train");
        File imagesValDir = new File(baseDir, "images/val");

        File labelsTrainDir = new File(baseDir, "labels/train");
        File labelsValDir = new File(baseDir, "labels/val");

        createDir(imagesTrainDir);
        createDir(imagesValDir);
        createDir(labelsTrainDir);
        createDir(labelsValDir);

        Map<String, Integer> labelMap = new HashMap<>();
        Map<String, Boolean> splitMap = new HashMap<>();

        Random random = new Random();

        for (Task task : assignment.getTasks()) {
            for (Annotation ann : task.getAnnotations()) {

                if (ann.getAnnotationData() == null
                        || ann.getAnnotationData().getRaw() == null) continue;

                Dataitem item = ann.getDataitem();
                String fileName = resolveFileName(item);

                splitMap.putIfAbsent(fileName, random.nextDouble() < 0.8);
                boolean isTrain = splitMap.get(fileName);

                File currentImageDir = isTrain ? imagesTrainDir : imagesValDir;
                File currentLabelDir = isTrain ? labelsTrainDir : labelsValDir;

                File labelFile = new File(currentLabelDir,
                        fileName.replaceAll("\\.[^.]+$", "") + ".txt");

                BufferedWriter writer = new BufferedWriter(new FileWriter(labelFile, true));

                for (Shape s : ann.getAnnotationData().getRaw()) {

                    if (s.getLabel() == null) continue;

                    labelMap.putIfAbsent(s.getLabel(), labelMap.size());
                    int classId = labelMap.get(s.getLabel());

                    // ======================
                    //  DETECT MODE (BBOX)
                    // ======================
                    if ("DETECT".equals(mode) && s instanceof BoundingBoxShape box) {

                        if (box.getWidth() == null || box.getHeight() == null) continue;

                        double x_center = (box.getX() + box.getWidth() / 2) / item.getWidth();
                        double y_center = (box.getY() + box.getHeight() / 2) / item.getHeight();
                        double w = box.getWidth() / item.getWidth();
                        double h = box.getHeight() / item.getHeight();

                        writer.write(classId + " " + x_center + " " + y_center + " " + w + " " + h);
                        writer.newLine();
                    }

                    // ======================
                    //  SEGMENT MODE (POLYGON)
                    // ======================
                    else if ("SEGMENT".equals(mode) && s instanceof PolygonShape polygon) {

                        if (polygon.getPoints() == null || polygon.getPoints().size() < 3) continue;

                        StringBuilder line = new StringBuilder();
                        line.append(classId).append(" ");

                        for (List<Double> p : polygon.getPoints()) {

                            if (p.size() < 2) continue;

                            double x = clamp(p.get(0) / item.getWidth());
                            double y = clamp(p.get(1) / item.getHeight());

                            line.append(x).append(" ").append(y).append(" ");
                        }

                        writer.write(line.toString().trim());
                        writer.newLine();
                    }
                }

                writer.close();

                // download image
                Path target = Paths.get(currentImageDir.getPath(), fileName);
                FileUtils.downloadImage(item.getUrl(), target);
            }
        }

        // classes.txt
        FileUtils.writeClassesFile(baseDir, labelMap);

        // dataset.yaml
        writeYaml(baseDir, labelMap);

        // zip
        File zipFile = new File(basePath + ".zip");
        FileUtils.zipFolder(baseDir, zipFile);

        return zipFile;
    }

    // ======================
    // HELPERS
    // ======================

    private void writeYaml(File baseDir, Map<String, Integer> labelMap) throws Exception {

        File yamlFile = new File(baseDir, "dataset.yaml");
        BufferedWriter writer = new BufferedWriter(new FileWriter(yamlFile));

        writer.write("train: ./images/train\n");
        writer.write("val: ./images/val\n");
        writer.write("nc: " + labelMap.size() + "\n");

        List<String> names = labelMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .toList();

        writer.write("names: [");
        writer.write(String.join(", ", names.stream().map(n -> "\"" + n + "\"").toList()));
        writer.write("]");

        writer.close();
    }

    private double clamp(double v) {
        return Math.max(0.0, Math.min(1.0, v));
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