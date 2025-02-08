package com.gianmarques.supporttracker.repository;

import com.gianmarques.supporttracker.entity.Client;
import com.gianmarques.supporttracker.entity.Support;
import com.gianmarques.supporttracker.entity.Ticket;
import com.gianmarques.supporttracker.entity.TicketAllocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketAllocationRepository extends JpaRepository<TicketAllocation, Long> {
    
    List<TicketAllocation> findAllBySupport(Support support);

    List<TicketAllocation> findAllByClient(Client client);
}
