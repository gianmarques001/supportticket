package com.gianmarques.supporticket.repository;

import com.gianmarques.supporticket.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
