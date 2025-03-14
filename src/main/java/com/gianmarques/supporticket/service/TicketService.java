package com.gianmarques.supporticket.service;

import com.gianmarques.supporticket.entity.Client;
import com.gianmarques.supporticket.entity.Support;
import com.gianmarques.supporticket.entity.Ticket;
import com.gianmarques.supporticket.entity.TicketAllocation;
import com.gianmarques.supporticket.exception.exceptions.TicketAlreadyClosedException;
import com.gianmarques.supporticket.repository.SupportRepository;
import com.gianmarques.supporticket.repository.TicketAllocationRepository;
import com.gianmarques.supporticket.repository.TicketRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TicketService {


    private final TicketAllocationRepository ticketAllocationRepository;
    private final TicketRepository ticketRepository;
    private final SupportService supportService;
    private final ClientService clientService;
    private final SupportRepository supportRepository;

    public TicketService(TicketAllocationRepository ticketAllocationRepository, TicketRepository ticketRepository, SupportService supportService, ClientService clientService, SupportRepository supportRepository) {
        this.ticketAllocationRepository = ticketAllocationRepository;
        this.ticketRepository = ticketRepository;
        this.supportService = supportService;
        this.clientService = clientService;
        this.supportRepository = supportRepository;
    }

    public Ticket saveTicket(Ticket ticket, Long id) {
        Client client = clientService.getClientById(id);
        ticket.setClient(client);
        return ticketRepository.save(ticket);
    }

    public List<Ticket> getAllOpenTickets() {
        List<Ticket> tickets = ticketRepository.findAll();
        List<Ticket> ticketsOpen = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (ticket.getStatus().equals(Ticket.Status.OPEN)) {
                ticketsOpen.add(ticket);
            }
        }
        return ticketsOpen;
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Ticket não encontrado."));
    }

    public void updateTicket(Long idTicket, Long idSupport) {


        Ticket ticket = getTicketById(idTicket);
        Support support = supportService.getSupportById(idSupport);
        if (ticket.getStatus().equals(Ticket.Status.OPEN)) {
            ticket.setStatus(Ticket.Status.IN_PROGRESS);
            ticket.setSupport(support);
            TicketAllocation ticketAllocation = new TicketAllocation();
            ticketAllocation.setTicket(ticket);
            ticketAllocation.setClient(ticket.getClient());
            ticketAllocation.setSupport(support);
            ticketAllocationRepository.save(ticketAllocation);
            return;
        }
        if (ticket.getStatus().equals(Ticket.Status.IN_PROGRESS)) {
            ticket.setStatus(Ticket.Status.CLOSED);

        } else {
            throw new TicketAlreadyClosedException("Ticket já está fechado.");
        }
    }


    public List<?> getAllTicketsBySupport(Long id) {
        Support support = supportService.getSupportById(id);
        List<TicketAllocation> tickets = ticketAllocationRepository.findAllBySupport(support);
        if (tickets.isEmpty()) {
            throw new EntityNotFoundException("Sem tickets.");
        }
        return supportRepository.findAllByIdEquals(id);
    }


    public List<Ticket> getTicketsClient(Long id) {

        Client client = clientService.getClientById(id);
        List<TicketAllocation> ticketsAllocation = ticketAllocationRepository.findAllByClient(client);
        List<Ticket> tickets = new ArrayList<>();
        ticketsAllocation.forEach(ticketAllocation -> tickets.add(ticketAllocation.getTicket()));
        return tickets;
    }
}
