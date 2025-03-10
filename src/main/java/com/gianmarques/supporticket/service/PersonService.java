package com.gianmarques.supporticket.service;


import com.gianmarques.supporticket.entity.Person;
import com.gianmarques.supporticket.exception.exceptions.PasswordInvalidException;
import com.gianmarques.supporticket.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class PersonService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;


    public PersonService(PersonRepository userRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = userRepository;
        this.passwordEncoder = passwordEncoder;


    }

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }


    public Person getPersonById(Long id) {
        return personRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada."));
    }

    public void deletePersonById(Long id) {
        Person person = getPersonById(id);
        if (person != null) {
            personRepository.delete(person);
        }
    }


    public Person updatePersonById(Long id, String oldPassword, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new PasswordInvalidException("Campo da nova senha está diferente do confirmar.");
        }
        Person person = getPersonById(id);
        if (!passwordEncoder.matches(oldPassword, person.getPassword())) {
            throw new PasswordInvalidException("Campo senha está incorreto.");
        }
        person.setPassword(passwordEncoder.encode(newPassword));
        return person;

    }

    public Person getPersonByEmail(String username) {
        return personRepository.findByEmail(username).orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada."));
    }

    public Person.Role getPersonByRole(String email) {
        return personRepository.findRoleByUsername(email);
    }
}


