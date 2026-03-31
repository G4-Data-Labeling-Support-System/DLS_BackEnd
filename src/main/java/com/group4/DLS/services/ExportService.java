package com.group4.DLS.services;

import com.group4.DLS.domain.dto.response.BoundingBoxShape;
import com.group4.DLS.domain.dto.response.PolygonShape;
import com.group4.DLS.domain.dto.response.Shape;
import com.group4.DLS.domain.entity.Annotation;
import com.group4.DLS.domain.entity.Assignment;
import com.group4.DLS.domain.entity.Task;
import com.group4.DLS.domain.enums.AssignmentStatus;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.repositories.AssignmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ExportService {

    AssignmentRepository assignmentRepository;
    YoloExportService yoloExportService;
    CocoExportService cocoExportService;
    JsonExportService jsonExport;

    public File export(String assignmentId, String format) throws Exception {

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND));

        //  FIX: phải throw
        if (assignment.getAssignmentStatus() != AssignmentStatus.COMPLETED) {
            throw new AppException(ErrorCode.ASSIGNMENT_NOT_COMPLETE_TO_EXPORT);
        }

        // ======================
        //  CHECK TYPE
        // ======================
        boolean hasBox = false;
        boolean hasPolygon = false;

        for (Task task : assignment.getTasks()) {
            for (Annotation ann : task.getAnnotations()) {

                if (ann.getAnnotationData() == null
                        || ann.getAnnotationData().getRaw() == null) continue;

                for (Shape s : ann.getAnnotationData().getRaw()) {

                    if (s instanceof BoundingBoxShape) hasBox = true;
                    if (s instanceof PolygonShape) hasPolygon = true;

                    // tối ưu: đủ 2 loại thì break sớm
                    if (hasBox && hasPolygon) break;
                }
            }
        }

        // ======================
        // YOLO RULE
        // ======================
        if ("yolo".equalsIgnoreCase(format)) {

            //  mix → chặn
            if (hasBox && hasPolygon) {
                throw new AppException(ErrorCode.ANNOTATION_MIXED_TYPE_NOT_SUPPORTED);
            }

            //  auto chọn mode
            String mode = hasPolygon ? "SEGMENT" : "DETECT";

            return yoloExportService.export(assignment, mode);
        }

        // ======================
        // COCO (support cả 2)
        // ======================
        if ("coco".equalsIgnoreCase(format)) {
            return cocoExportService.export(assignment);
        }

        // ======================
        // JSON (support cả 2)
        // ======================
        if ("json".equalsIgnoreCase(format)) {
            return jsonExport.jsonExport(assignment);
        }

        throw new RuntimeException("Format chưa support");
    }
}