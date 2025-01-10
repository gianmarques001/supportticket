package com.gianmarques.supporttracker.mapper;


import com.gianmarques.supporttracker.mapper.dto.client.ClientResponseDto;
import com.gianmarques.supporttracker.mapper.dto.person.PersonRequestDto;
import com.gianmarques.supporttracker.entity.Client;
import org.modelmapper.ModelMapper;

public class ClientMapper {


    public static ClientResponseDto toDto(Client client) {
        return new ModelMapper().map(client, ClientResponseDto.class);
    }

    public static Client toClient(PersonRequestDto personRequestDto) {
        return new ModelMapper().map(personRequestDto, Client.class);
    }
}


