package com.gianmarques.supporttracker.service;

import com.gianmarques.supporttracker.entity.Client;
import com.gianmarques.supporttracker.exception.exceptions.EmailUniqueException;
import com.gianmarques.supporttracker.exception.exceptions.PasswordInvalidException;
import com.gianmarques.supporttracker.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClientService {


    private final PasswordEncoder passwordEncoder;

    private final ClientRepository clientRepository;

    public ClientService(PasswordEncoder passwordEncoder, ClientRepository clientRepository) {
        this.passwordEncoder = passwordEncoder;
        this.clientRepository = clientRepository;
    }

    public Client addClient(Client client) {
        try {
            client.setPassword(passwordEncoder.encode(client.getPassword()));
            return clientRepository.save(client);
        } catch (DataIntegrityViolationException e) {
            throw new EmailUniqueException("Email already registered. " + client.getEmail());
        }

    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Client not found"));

    }


    public Client updateClientById(Long id, String oldPassword, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new PasswordInvalidException("Passwords are different.");
        }
        Client client = getClientById(id);
        if (!passwordEncoder.matches(oldPassword, client.getPassword())) {
            throw new PasswordInvalidException("Password is incorrect.");
        }
        client.setPassword(passwordEncoder.encode(newPassword));
        return client;

    }
}
