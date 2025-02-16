package com.gianmarques.supporticket.mapper.dto.client;

public class ClientResponseDto {

    private String name;

    private String email;


    public ClientResponseDto() {
    }

    public ClientResponseDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
