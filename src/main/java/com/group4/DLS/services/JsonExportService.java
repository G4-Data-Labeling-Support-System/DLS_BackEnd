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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class JsonExportService {

    ObjectMapper mapper;

    public File jsonExport(Assignment assignment) throws Exception {

        String basePath = System.getProperty("java.io.tmpdir")
                + "/json_" + assignment.getAssignmentName() + "_" + System.currentTimeMillis();

        File baseDir = new File(basePath);
        createDir(baseDir);

        File jsonFile = new File(baseDir, "annotations.json");

        List<Map<String, Object>> result = new ArrayList<>();

        for (Task task : assignment.getTasks()) {
            for (Annotation ann : task.getAnnotations()) {

                if (ann.getAnnotationData() == null) continue;


                if (ann.getAnnotationData().getRaw() == null) continue;

                Dataitem item = ann.getDataitem();

                // tìm image đã có chưa
                Map<String, Object> imageMap = result.stream()
                        .filter(m -> m.get("fileName").equals(item.getFileName()))
                        .findFirst()
                        .orElse(null);

                if (imageMap == null) {
                    imageMap = new HashMap<>();
                    imageMap.put("fileName", item.getFileName());
                    imageMap.put("imageUrl", item.getUrl());
                    imageMap.put("width", item.getWidth());
                    imageMap.put("height", item.getHeight());
                    imageMap.put("annotations", new ArrayList<>());

                    result.add(imageMap);
                }

                @SuppressWarnings("unchecked")
                List<Map<String, Object>> annList =
                        (List<Map<String, Object>>) imageMap.get("annotations");

                for (Shape s : ann.getAnnotationData().getRaw()) {

                    Map<String, Object> shapeMap = new HashMap<>();
                    shapeMap.put("label", s.getLabel());
                    shapeMap.put("type", s.getType());
                    shapeMap.put("x", s.getX());
                    shapeMap.put("y", s.getY());
                    shapeMap.put("width", s.getWidth());
                    shapeMap.put("height", s.getHeight());

                    annList.add(shapeMap);
                }
            }
        }

        // ghi file JSON
        mapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, result);

        // zip
        File zipFile = new File(basePath + ".zip");
        FileUtils.zipFolder(baseDir, zipFile);

        return zipFile;
    }

    private void createDir(File dir) {
        if (!dir.exists() && !dir.mkdirs()) {
            throw new RuntimeException("Không tạo được thư mục: " + dir.getAbsolutePath());
        }
    }
}
