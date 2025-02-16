package com.gianmarques.supporticket.security.jwt;

import com.gianmarques.supporticket.entity.Person;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class JwtUserDetails extends User {

    private Person person;

    public JwtUserDetails(Person person) {
        super(person.getEmail(), person.getPassword(), AuthorityUtils.createAuthorityList(person.getRole().name()));
        this.person = person;
    }

    public Long getId() {
        return this.person.getId();
    }

    public String getRole() {
        return this.person.getRole().name();
    }
}
