package com.gianmarques.supporticket.repository;

import com.gianmarques.supporticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {



}
