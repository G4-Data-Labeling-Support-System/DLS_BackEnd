package com.group4.DLS.services;

import com.group4.DLS.domain.entity.Assignment;
import com.group4.DLS.domain.enums.AssignmentStatus;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.repositories.AssignmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.io.*;

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

        if(assignment.getAssignmentStatus() != AssignmentStatus.COMPLETED){
            new AppException(ErrorCode.ASSIGNMENT_NOT_COMPLETE_TO_EXPORT);
        }

        if ("yolo".equalsIgnoreCase(format)) {
            return yoloExportService.export(assignment);
        }

        if ("coco".equalsIgnoreCase(format)) {
            return cocoExportService.export(assignment);
        }

        if ("json".equalsIgnoreCase(format)) {
            return jsonExport.jsonExport(assignment);
        }

        throw new RuntimeException("Format chưa support");
    }
}

