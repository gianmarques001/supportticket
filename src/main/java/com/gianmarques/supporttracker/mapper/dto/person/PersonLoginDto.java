package com.gianmarques.supporttracker.mapper.dto.person;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PersonLoginDto {

    @NotNull
    private String email;

    @NotNull
    @Size(min = 8, max = 16)
    private String password;

    public PersonLoginDto() {
    }

    public PersonLoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public @NotNull String getEmail() {
        return email;
    }

    public void setEmail(@NotNull String email) {
        this.email = email;
    }

    public @NotNull @Size(min = 8) String getPassword() {
        return password;
    }

    public void setPassword(@NotNull @Size(min = 8) String password) {
        this.password = password;
    }
}
