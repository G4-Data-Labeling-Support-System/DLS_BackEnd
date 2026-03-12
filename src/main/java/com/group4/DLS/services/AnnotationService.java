package com.group4.DLS.services;

import com.group4.DLS.domain.entity.Annotation;
import com.group4.DLS.domain.entity.Dataitem;
import com.group4.DLS.domain.entity.Label;
import com.group4.DLS.domain.entity.Task;
import com.group4.DLS.domain.enums.AnnotationStatus;
import com.group4.DLS.repositories.AnnotationRepository;
import com.group4.DLS.repositories.LabelRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AnnotationService {

    AnnotationRepository annotationRepository;
    LabelRepository labelRepository;

    //assign annotation to task
    public void assignAnnotationToTask(Task task, List<Dataitem> dataitems){
        List<Annotation> list = new ArrayList<>();

        List<Label> labels = labelRepository.findByDataset_DatasetId(task.getAssignment().getDataset().getDatasetId());
        for(Dataitem item : dataitems){
            Annotation annotation = new Annotation();
            annotation.setTask(task);
            annotation.setUser(task.getAssignment().getAssignedTo());
            annotation.setAnnotationStatus(AnnotationStatus.DRAFT);
            annotation.setLabels(labels);
            annotation.setDataitem(item);

            list.add(annotation);
        }
        annotationRepository.saveAll(list);
    }
}
