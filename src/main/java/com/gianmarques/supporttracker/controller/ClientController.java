package com.gianmarques.supporttracker.controller;

import com.gianmarques.supporttracker.mapper.dto.client.ClientResponseDto;
import com.gianmarques.supporttracker.mapper.dto.client.ClientUpdateDto;
import com.gianmarques.supporttracker.mapper.ClientMapper;
import com.gianmarques.supporttracker.mapper.dto.person.PersonRequestDto;
import com.gianmarques.supporttracker.entity.Client;
import com.gianmarques.supporttracker.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {


    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ClientResponseDto> saveClient(@Valid @RequestBody PersonRequestDto personRequestDto) {
        Client client = clientService.addClient(ClientMapper.toClient(personRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(ClientMapper.toDto(client));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<?> updateClient(@PathVariable Long id, @Valid @RequestBody ClientUpdateDto clientUpdateDto) {
        Client client = clientService.updateClientById(id, clientUpdateDto.getOldPassword(), clientUpdateDto.getNewPassword(), clientUpdateDto.getConfirmPassword());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
