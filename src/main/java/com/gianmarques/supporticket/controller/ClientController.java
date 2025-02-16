package com.gianmarques.supporticket.controller;

import com.gianmarques.supporticket.entity.Client;
import com.gianmarques.supporticket.exception.model.ErrorMessage;
import com.gianmarques.supporticket.mapper.ClientMapper;
import com.gianmarques.supporticket.mapper.dto.client.ClientResponseDto;
import com.gianmarques.supporticket.mapper.dto.client.ClientUpdateDto;
import com.gianmarques.supporticket.mapper.dto.person.PersonRequestDto;
import com.gianmarques.supporticket.service.ClientService;
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
import org.springframework.web.bind.annotation.*;


@Tag(name = "Clientes", description = "Recursos para salvar e atualizar clientes.")
@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Operation(summary = "Salvar cliente.", description = "Recurso para salvar cliente.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Salvar cliente.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ClientResponseDto.class))),
                    @ApiResponse(responseCode = "409", description = "Conflito de dados.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Campos inválidos.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
            })
    @PostMapping
    public ResponseEntity<ClientResponseDto> saveClient(@Valid @RequestBody PersonRequestDto personRequestDto) {
        Client client = clientService.addClient(ClientMapper.toClient(personRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(ClientMapper.toDto(client));
    }


    @Operation(summary = "Atualizar cliente.", description = "Recurso para atualizar um cliente. (Apenas role de admin pode acessar)",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Atualizar cliente."),
                    @ApiResponse(responseCode = "400", description = "Campo da nova senha está divergente.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado.",
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
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateClient(@PathVariable Long id, @Valid @RequestBody ClientUpdateDto clientUpdateDto) {
        clientService.updateClientById(id, clientUpdateDto.getOldPassword(), clientUpdateDto.getNewPassword(), clientUpdateDto.getConfirmPassword());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
