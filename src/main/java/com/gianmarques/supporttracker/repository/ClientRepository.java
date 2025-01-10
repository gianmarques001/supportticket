package com.gianmarques.supporttracker.repository;

import com.gianmarques.supporttracker.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
