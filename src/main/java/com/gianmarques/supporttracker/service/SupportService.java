package com.gianmarques.supporttracker.service;

import com.gianmarques.supporttracker.entity.Person;
import com.gianmarques.supporttracker.entity.Support;
import com.gianmarques.supporttracker.exception.exceptions.EmailUniqueException;
import com.gianmarques.supporttracker.repository.SupportRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SupportService {

    private final SupportRepository supportRepository;
    private final PasswordEncoder passwordEncoder;

    public SupportService(SupportRepository supportRepository, PasswordEncoder passwordEncoder) {
        this.supportRepository = supportRepository;
        this.passwordEncoder = passwordEncoder;

    }

    public Support getSupportById(Long id) {
        return supportRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Support not found"));
    }


    public Support addSupport(Support support) {
        try {
            support.setPassword(passwordEncoder.encode(support.getPassword()));
            support.setRole(Person.Role.ROLE_SUPPORT);
            return supportRepository.save(support);
        } catch (DataIntegrityViolationException e) {
            throw new EmailUniqueException("Email already registered. " + support.getEmail());
        }
    }


}
