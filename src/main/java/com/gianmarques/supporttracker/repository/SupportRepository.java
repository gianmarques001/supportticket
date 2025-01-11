package com.gianmarques.supporttracker.repository;

import com.gianmarques.supporttracker.entity.Support;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportRepository extends JpaRepository<Support, Long> {


    List<Support> findAllByIdEquals(Long id);
}
