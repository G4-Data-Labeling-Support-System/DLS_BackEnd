package com.group4.DLS.repository;

import com.group4.DLS.domain.entity.Dataset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatasetRepository extends JpaRepository<Dataset, String> {
}
