package com.gianmarques.supporttracker.repository;

import com.gianmarques.supporttracker.entity.Support;
import com.gianmarques.supporttracker.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SupportRepository extends JpaRepository<Support, Long> {

//    @Query("select t.support from Support t where t.support.id = :id")
    List<Support> findAllByIdEquals(Long id);
}
