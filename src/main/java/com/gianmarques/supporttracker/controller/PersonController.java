package com.gianmarques.supporttracker.controller;


import com.gianmarques.supporttracker.entity.Person;
import com.gianmarques.supporttracker.mapper.PersonMapper;
import com.gianmarques.supporttracker.mapper.dto.person.PersonRequestDto;
import com.gianmarques.supporttracker.mapper.dto.person.PersonResponseDto;
import com.gianmarques.supporttracker.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<PersonResponseDto>> getPersons() {
        List<Person> persons = personService.getAllPersons();
        return ResponseEntity.status(HttpStatus.OK).body(PersonMapper.toListDTO(persons));
    }

    @PostMapping
    public ResponseEntity<PersonResponseDto> savePerson(@Valid @RequestBody PersonRequestDto personRequestDto) {
        Person person = personService.addPerson(PersonMapper.toPerson(personRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(PersonMapper.toDto(person));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<PersonResponseDto> getPerson(@PathVariable Long id) {
        Person person = personService.getPersonById(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(PersonMapper.toDto(person));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<PersonResponseDto> deletePerson(@PathVariable Long id) {
        personService.deletePersonById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
