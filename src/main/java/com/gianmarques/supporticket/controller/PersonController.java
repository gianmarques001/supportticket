package com.gianmarques.supporticket.controller;


import com.gianmarques.supporticket.entity.Person;
import com.gianmarques.supporticket.exception.model.ErrorMessage;
import com.gianmarques.supporticket.mapper.PersonMapper;
import com.gianmarques.supporticket.mapper.dto.person.PersonResponseDto;
import com.gianmarques.supporticket.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Pessoas", description = "Recursos para listar pessoas e procurar por ID. (Apenas role de admins pode acessar)")
@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }


    @Operation(summary = "Listar pessoas.", description = "Recurso para listar as pessoas.",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Retorna as pessoas.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PersonResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Acesso não autorizado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Acesso proibido.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<PersonResponseDto>> getPersons() {
        List<Person> persons = personService.getAllPersons();
        return ResponseEntity.status(HttpStatus.OK).body(PersonMapper.toListDTO(persons));
    }


    @Operation(summary = "Buscar pessoa por ID.", description = "Recurso para buscar pessoa pelo ID.",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Retorna pessoa.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PersonResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Acesso não autorizado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Acesso proibido",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Pessoa não encontrada.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<PersonResponseDto> getPerson(@PathVariable Long id) {
        Person person = personService.getPersonById(id);
        return ResponseEntity.status(HttpStatus.OK).body(PersonMapper.toDto(person));
    }

}
