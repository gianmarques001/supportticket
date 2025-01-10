package com.gianmarques.supporttracker.repository;

import com.gianmarques.supporttracker.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByEmail(String username);


    @Query("select p.role from Person p where p.email like :email")
    Person.Role findRoleByUsername(String email);
}
