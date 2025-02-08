package com.gianmarques.supporttracker.controller;

import com.gianmarques.supporttracker.entity.Client;
import com.gianmarques.supporttracker.exception.model.ErrorMessage;
import com.gianmarques.supporttracker.mapper.ClientMapper;
import com.gianmarques.supporttracker.mapper.dto.client.ClientResponseDto;
import com.gianmarques.supporttracker.mapper.dto.client.ClientUpdateDto;
import com.gianmarques.supporttracker.mapper.dto.person.PersonRequestDto;
import com.gianmarques.supporttracker.service.ClientService;
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


@Tag(name = "Clients", description = "Resources to save and update a client")
@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Operation(summary = "Save client", description = "Resource to save client in the system.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Create client",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ClientResponseDto.class))),
                    @ApiResponse(responseCode = "409", description = "Conflict data",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Invalid field error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
            })
    @PostMapping
    public ResponseEntity<ClientResponseDto> saveClient(@Valid @RequestBody PersonRequestDto personRequestDto) {
        Client client = clientService.addClient(ClientMapper.toClient(personRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(ClientMapper.toDto(client));
    }


    @Operation(summary = "Update client", description = "Resource to update client in the system.",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Update client"),
                    @ApiResponse(responseCode = "400", description = "The field in the new password are not the same.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Client not found.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "409", description = "Password is incorrect",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Invalid field error",
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
