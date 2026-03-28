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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class YoloExportService {

    ObjectMapper mapper;

    public File export(Assignment assignment) throws Exception {

        String basePath = System.getProperty("java.io.tmpdir") + assignment.getAssignmentName()
                + "/yolo_" + System.currentTimeMillis();

        File baseDir = new File(basePath);

        File imagesTrainDir = new File(baseDir, "images/train");
        File imagesValDir = new File(baseDir, "images/val");

        File labelsTrainDir = new File(baseDir, "labels/train");
        File labelsValDir = new File(baseDir, "labels/val");

        imagesTrainDir.mkdirs();
        imagesValDir.mkdirs();
        labelsTrainDir.mkdirs();
        labelsValDir.mkdirs();

        Map<String, Integer> labelMap = new HashMap<>();
        Map<String, Boolean> splitMap = new HashMap<>();

        Random random = new Random();

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

                // FIX: đảm bảo 1 image chỉ thuộc train hoặc val
                splitMap.putIfAbsent(fileName, random.nextDouble() < 0.8);
                boolean isTrain = splitMap.get(fileName);

                File currentImageDir = isTrain ? imagesTrainDir : imagesValDir;
                File currentLabelDir = isTrain ? labelsTrainDir : labelsValDir;

                // 📄 label file
                File labelFile = new File(currentLabelDir,
                        fileName.replaceAll("\\.[^.]+$", "") + ".txt");

                BufferedWriter writer = new BufferedWriter(new FileWriter(labelFile, true));

                for (Shape s : data.getShapes()) {

                    if (s.getLabel() == null) continue;

                    labelMap.putIfAbsent(s.getLabel(), labelMap.size());
                    int classId = labelMap.get(s.getLabel());

                    double x_center = (s.getX() + s.getWidth() / 2) / item.getWidth();
                    double y_center = (s.getY() + s.getHeight() / 2) / item.getHeight();
                    double w = s.getWidth() / item.getWidth();
                    double h = s.getHeight() / item.getHeight();

                    String line = classId + " " + x_center + " " + y_center + " " + w + " " + h;

                    writer.write(line);
                    writer.newLine();
                }

                writer.close();

                // 🖼 download ảnh
                Path target = Paths.get(currentImageDir.getPath(), fileName);
                FileUtils.downloadImage(item.getUrl(), target);
            }
        }

        // classes.txt
        FileUtils.writeClassesFile(baseDir, labelMap);

        // dataset.yaml
        File yamlFile = new File(baseDir, "dataset.yaml");
        BufferedWriter yamlWriter = new BufferedWriter(new FileWriter(yamlFile));

        yamlWriter.write("train: ./images/train");
        yamlWriter.newLine();
        yamlWriter.write("val: ./images/val");
        yamlWriter.newLine();

        yamlWriter.write("nc: " + labelMap.size());
        yamlWriter.newLine();

        yamlWriter.write("names: [");

        List<String> names = labelMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .toList();

        String nameStr = names.stream()
                .map(n -> "\"" + n + "\"")
                .reduce((a, b) -> a + ", " + b)
                .orElse("");

        yamlWriter.write(nameStr);
        yamlWriter.write("]");
        yamlWriter.close();

        // 📦 zip
        File zipFile = new File(basePath + ".zip");
        FileUtils.zipFolder(baseDir, zipFile);

        return zipFile;
    }

    private String resolveFileName(Dataitem item) {
        if (item.getFileName() != null && item.getFileName().contains(".")) {
            return item.getFileName();
        }
        String url = item.getUrl();
        String name = url.substring(url.lastIndexOf("/") + 1);
        return name.replace(",", "_") + ".jpg";
    }
}
