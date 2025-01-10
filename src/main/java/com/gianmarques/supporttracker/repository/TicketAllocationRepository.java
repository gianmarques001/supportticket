package com.gianmarques.supporttracker.repository;

import com.gianmarques.supporttracker.entity.TicketAllocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketAllocationRepository extends JpaRepository<TicketAllocation, Long> {
}
