package com.gianmarques.supporticket.mapper;


import com.gianmarques.supporticket.mapper.dto.client.ClientResponseDto;
import com.gianmarques.supporticket.mapper.dto.person.PersonRequestDto;
import com.gianmarques.supporticket.entity.Client;
import org.modelmapper.ModelMapper;

public class ClientMapper {


    public static ClientResponseDto toDto(Client client) {
        return new ModelMapper().map(client, ClientResponseDto.class);
    }

    public static Client toClient(PersonRequestDto personRequestDto) {
        return new ModelMapper().map(personRequestDto, Client.class);
    }
}


