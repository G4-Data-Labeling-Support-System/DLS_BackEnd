package com.group4.DLS.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.group4.DLS.domain.entity.Review;

import jakarta.transaction.Transactional;

import java.util.List;


@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {
    List<Review> findByAnnotation_AnnotationId(String annotationId);

    // Remove review by annotation_id
    @Transactional
    void deleteByAnnotation_AnnotationIdIn(List<String> annotationIds);
}
