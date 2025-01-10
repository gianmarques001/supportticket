package com.gianmarques.supporttracker.controller;


import com.gianmarques.supporttracker.entity.Ticket;
import com.gianmarques.supporttracker.mapper.TicketMapper;
import com.gianmarques.supporttracker.mapper.dto.ticket.TicketListResponseDto;
import com.gianmarques.supporttracker.mapper.dto.ticket.TicketRequestDto;
import com.gianmarques.supporttracker.mapper.dto.ticket.TicketResponseDto;
import com.gianmarques.supporttracker.security.jwt.JwtUserDetails;
import com.gianmarques.supporttracker.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PreAuthorize("hasRole('ADMIN') OR hasRole('SUPPORT')")
    @GetMapping
    public ResponseEntity<List<TicketListResponseDto>> getAllTickets() {
        List<Ticket> tickets = ticketService.getAllOpenTickets();
        List<TicketListResponseDto> ticketsOpen = TicketMapper.toList(tickets);
        return ResponseEntity.status(HttpStatus.OK).body(ticketsOpen);
    }


    @PreAuthorize("hasRole('SUPPORT')")
    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDto> getTicket(@PathVariable Long id) {
        Ticket ticket = ticketService.getTicketById(id);
        return ResponseEntity.status(HttpStatus.OK).body(TicketMapper.toDto(ticket));
    }


    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping
    public ResponseEntity<TicketResponseDto> sendTicket(@AuthenticationPrincipal JwtUserDetails userDetails, @Valid @RequestBody TicketRequestDto ticketRequestDto) {
        Ticket ticket = ticketService.saveTicket(TicketMapper.toTicket(ticketRequestDto), userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(TicketMapper.toDto(ticket));
    }


    @PreAuthorize("hasRole('SUPPORT')")
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateTicket(@PathVariable Long id, @AuthenticationPrincipal JwtUserDetails userDetails) {
        ticketService.updateTicket(id, userDetails.getId());
        return ResponseEntity.ok().build();
    }




}
