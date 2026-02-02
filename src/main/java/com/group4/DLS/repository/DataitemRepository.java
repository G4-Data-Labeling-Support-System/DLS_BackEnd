package com.group4.DLS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.group4.DLS.domain.entity.Dataitem;

@Repository
public interface DataitemRepository extends JpaRepository<Dataitem, String> {

}