package com.gianmarques.supporticket.controller;


import com.gianmarques.supporticket.entity.Ticket;
import com.gianmarques.supporticket.exception.model.ErrorMessage;
import com.gianmarques.supporticket.mapper.TicketMapper;
import com.gianmarques.supporticket.mapper.dto.ticket.TicketClientResponseDto;
import com.gianmarques.supporticket.mapper.dto.ticket.TicketListResponseDto;
import com.gianmarques.supporticket.mapper.dto.ticket.TicketRequestDto;
import com.gianmarques.supporticket.mapper.dto.ticket.TicketResponseDto;
import com.gianmarques.supporticket.security.jwt.JwtUserDetails;
import com.gianmarques.supporticket.service.TicketService;
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
@Tag(name = "Tickets", description = "Recursos para gerenciar tickets do sistema.")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Operation(summary = "Listar todos os tickets.", description = "Recurso para listar todos os tickets que estão com o status OPEN no sistema. (Roles de admin e support podem acessar)",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Retorna os tickets.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TicketListResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Acesso não autorizado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Acesso proibido.",
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


    @Operation(summary = "Buscar ticket por ID.", description = "Recurso para buscar ticket pelo ID. (Apenas role de support pode acessar)",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Retorna ticket.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TicketListResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Acesso não autorizado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Acesso proibido.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Ticket não encontrado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PreAuthorize("hasRole('SUPPORT')")
    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDto> getTicket(@PathVariable Long id) {
        Ticket ticket = ticketService.getTicketById(id);
        return ResponseEntity.status(HttpStatus.OK).body(TicketMapper.toDto(ticket));
    }


    @Operation(summary = "Cliente listar todos os seus tickets.", description = "Recurso para o cliente listar todos os seus tickets. (Apenas role de cliente pode acessar)",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Retorna tickets",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TicketListResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Acesso não autorizado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Acesso proibido.",
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


    @Operation(summary = "Enviar ticket", description = "Cliente envia ticket. (Apenas role de client pode acessar)",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Envio de ticket.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TicketResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Acesso não autorizado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Acesso proibido.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),

            })
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping
    public ResponseEntity<TicketResponseDto> sendTicket(@AuthenticationPrincipal JwtUserDetails userDetails, @Valid @RequestBody TicketRequestDto ticketRequestDto) {
        Ticket ticket = ticketService.saveTicket(TicketMapper.toTicket(ticketRequestDto), userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(TicketMapper.toDto(ticket));
    }


    @Operation(summary = "Atualizar ticket por ID.", description = "Recurso para atualizar ticket pelo ID. (Apenas role de support pode acessar)",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Atualiza ticket"),
                    @ApiResponse(responseCode = "401", description = "Acesso não autorizado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Acesso proibido.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Ticket não encontrado.",
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
