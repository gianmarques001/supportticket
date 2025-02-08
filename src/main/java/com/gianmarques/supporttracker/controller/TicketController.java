package com.gianmarques.supporttracker.controller;


import com.gianmarques.supporttracker.entity.Ticket;
import com.gianmarques.supporttracker.exception.model.ErrorMessage;
import com.gianmarques.supporttracker.mapper.TicketMapper;
import com.gianmarques.supporttracker.mapper.dto.ticket.TicketClientResponseDto;
import com.gianmarques.supporttracker.mapper.dto.ticket.TicketListResponseDto;
import com.gianmarques.supporttracker.mapper.dto.ticket.TicketRequestDto;
import com.gianmarques.supporttracker.mapper.dto.ticket.TicketResponseDto;
import com.gianmarques.supporttracker.security.jwt.JwtUserDetails;
import com.gianmarques.supporttracker.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
@Tag(name = "Tickets", description = "Resource to manage tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Operation(summary = "Get all tickets", description = "Resource to get all tickets that have OPEN status.",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Return tickets",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TicketListResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden access",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PreAuthorize("hasRole('ADMIN') OR hasRole('SUPPORT')")
    @GetMapping
    public ResponseEntity<List<TicketListResponseDto>> getAllTickets() {
        List<Ticket> tickets = ticketService.getAllOpenTickets();
        List<TicketListResponseDto> ticketsOpen = TicketMapper.toList(tickets);
        return ResponseEntity.status(HttpStatus.OK).body(ticketsOpen);
    }


    @Operation(summary = "Get ticket by ID", description = "Resource to get ticket by ID.",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Return ticket",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TicketListResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden access",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Ticket not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PreAuthorize("hasRole('SUPPORT')")
    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDto> getTicket(@PathVariable Long id) {
        Ticket ticket = ticketService.getTicketById(id);
        return ResponseEntity.status(HttpStatus.OK).body(TicketMapper.toDto(ticket));
    }


    @Operation(summary = "Client get ticket all  your tickets.", description = "Resource to client get all your tickets.",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Return tickets",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TicketListResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden access",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
            })
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/client")
    public ResponseEntity<List<TicketClientResponseDto>> getTicketsClient(@AuthenticationPrincipal JwtUserDetails jwtUserDetails) {
        Long id = jwtUserDetails.getId();
        List<Ticket> tickets = ticketService.getTicketsClient(id);
        return ResponseEntity.status(HttpStatus.OK).body(TicketMapper.toListClient(tickets));

    }


    @Operation(summary = "Send ticket", description = "Client send ticket.",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Send ticket",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TicketResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden access",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),

            })
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping
    public ResponseEntity<TicketResponseDto> sendTicket(@AuthenticationPrincipal JwtUserDetails userDetails, @Valid @RequestBody TicketRequestDto ticketRequestDto) {
        Ticket ticket = ticketService.saveTicket(TicketMapper.toTicket(ticketRequestDto), userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(TicketMapper.toDto(ticket));
    }


    @Operation(summary = "Update ticket by ID", description = "Resource to update ticket by ID.",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Update ticket"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden access",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Ticket not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PreAuthorize("hasRole('SUPPORT')")
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateTicket(@PathVariable Long id, @AuthenticationPrincipal JwtUserDetails userDetails) {
        ticketService.updateTicket(id, userDetails.getId());
        return ResponseEntity.ok().build();
    }


}
