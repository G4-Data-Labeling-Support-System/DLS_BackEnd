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
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class YoloExportService {

    ObjectMapper mapper;

    public File export(Assignment assignment) throws Exception {

        String basePath = "exports/yolo_" + assignment.getAssignmentId();

        File baseDir = new File(basePath);
        File imagesDir = new File(baseDir, "images");
        File labelsDir = new File(baseDir, "labels");

        imagesDir.mkdirs();
        labelsDir.mkdirs();

        Map<String, Integer> labelMap = new HashMap<>();

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

                File labelFile = new File(labelsDir,
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

                // download ảnh
                Path target = Paths.get(imagesDir.getPath(), fileName);
                FileUtils.downloadImage(item.getUrl(), target);
            }
        }

        // classes.txt
        FileUtils.writeClassesFile(baseDir, labelMap);

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
        String name = url.substring(url.lastIndexOf("/") + 1);
        return name.replace(",", "_") + ".jpg";
    }
}
