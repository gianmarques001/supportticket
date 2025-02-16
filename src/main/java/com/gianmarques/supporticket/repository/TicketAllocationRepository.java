package com.gianmarques.supporticket.repository;

import com.gianmarques.supporticket.entity.Client;
import com.gianmarques.supporticket.entity.Support;
import com.gianmarques.supporticket.entity.TicketAllocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketAllocationRepository extends JpaRepository<TicketAllocation, Long> {
    
    List<TicketAllocation> findAllBySupport(Support support);

    List<TicketAllocation> findAllByClient(Client client);
}
