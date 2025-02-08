package com.gianmarques.supporttracker.security.jwt;


import com.gianmarques.supporttracker.entity.Person;
import com.gianmarques.supporttracker.service.PersonService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {


    private final PersonService personService;

    public JwtUserDetailsService(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Person person = personService.getPersonByEmail(email);
        return new JwtUserDetails(person);
    }


    // Quando o usuario for autenticar na aplicacao
    public JwtToken getAuthenticatedToken(String email) {
        Person.Role role = personService.getPersonByRole(email);
        System.out.println("ROLE: " + role.name());
        return JwtUtils.createToken(email, role.name().substring("ROLE_".length()));
    }
}
