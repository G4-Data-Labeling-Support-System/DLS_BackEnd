package com.group4.DLS.services;

import com.group4.DLS.domain.entity.Annotation;
import com.group4.DLS.domain.entity.Dataitem;
import com.group4.DLS.domain.entity.Task;
import com.group4.DLS.repositories.AnnotationRepository;
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

    //assign annotation to task
    public void assignAnnotationToTask(Task task, List<Dataitem> dataitems){
        List<Annotation> list = new ArrayList<>();

        for(Dataitem item : dataitems){
            Annotation annotation = new Annotation();
            annotation.setTask(task);
            annotation.setDataitem(item);

            list.add(annotation);
        }
        annotationRepository.saveAll(list);
    }
}
