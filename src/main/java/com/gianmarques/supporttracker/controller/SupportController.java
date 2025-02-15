package com.gianmarques.supporttracker.controller;


import com.gianmarques.supporttracker.entity.Support;
import com.gianmarques.supporttracker.exception.model.ErrorMessage;
import com.gianmarques.supporttracker.mapper.SupportMapper;
import com.gianmarques.supporttracker.mapper.dto.person.PersonRequestDto;
import com.gianmarques.supporttracker.mapper.dto.person.PersonResponseDto;
import com.gianmarques.supporttracker.mapper.dto.support.SupportListResponseDto;
import com.gianmarques.supporttracker.security.jwt.JwtUserDetails;
import com.gianmarques.supporttracker.service.SupportService;
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

@Tag(name = "Suporte", description = "Recursos para salvar um suporte e listar todos os tickets enviados no sistema.")
@RestController
@RequestMapping("/api/v1/support")
public class SupportController {

    private final SupportService supportService;
    private final TicketService ticketService;

    public SupportController(SupportService supportService, TicketService ticketService) {
        this.supportService = supportService;
        this.ticketService = ticketService;
    }

    @Operation(summary = "Salvar suporte.", description = "Recurso para salvar suporte no sistema. (Apenas role de admin pode acessar).",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = " support",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PersonResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Acesso não autorizado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Acesso proibido.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "409", description = "Conflito de dados.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Campos inválidos.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
            })

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PersonResponseDto> saveSupport(@Valid @RequestBody PersonRequestDto personRequestDto) {
        Support support = supportService.addSupport(SupportMapper.toSupport(personRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(SupportMapper.toDto(support));
    }


    @Operation(summary = "Listar todos os tickets.", description = "Recurso para listar todos os tickets aceitos pelo suporte. (Apenas role de support pode acessar)",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Retorna todos os tickets.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SupportListResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Acesso não autorizado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Acesso proibido.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PreAuthorize("hasRole('SUPPORT')")
    @GetMapping
    public ResponseEntity<List<SupportListResponseDto>> getAllTicketsBySupport(@AuthenticationPrincipal JwtUserDetails userDetails) {
        List<?> ticketsBySupport = ticketService.getAllTicketsBySupport(userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK).body(SupportMapper.toList((List<Support>) ticketsBySupport));

    }
}
