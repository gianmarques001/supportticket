package com.gianmarques.supporttracker.repository;

import com.gianmarques.supporttracker.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {


}
