package com.gianmarques.supporticket.repository;

import com.gianmarques.supporticket.entity.Support;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportRepository extends JpaRepository<Support, Long> {


    List<Support> findAllByIdEquals(Long id);
}
