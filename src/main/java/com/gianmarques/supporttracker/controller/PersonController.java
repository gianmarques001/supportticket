package com.gianmarques.supporttracker.controller;


import com.gianmarques.supporttracker.entity.Person;
import com.gianmarques.supporttracker.exception.model.ErrorMessage;
import com.gianmarques.supporttracker.mapper.PersonMapper;
import com.gianmarques.supporttracker.mapper.dto.person.PersonResponseDto;
import com.gianmarques.supporttracker.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Persons", description = "Resources to list all persons or by id and delete a person")
@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }


    @Operation(summary = "Get all persons", description = "Resource to get all persons.",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Return persons",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PersonResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden access",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<PersonResponseDto>> getPersons() {
        List<Person> persons = personService.getAllPersons();
        return ResponseEntity.status(HttpStatus.OK).body(PersonMapper.toListDTO(persons));
    }


    @Operation(summary = "Get person by ID", description = "Resource to get person by ID.",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Return person",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PersonResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden access",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Person not found",
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
