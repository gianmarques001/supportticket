package com.gianmarques.supporttracker.mapper;

import com.gianmarques.supporttracker.entity.Person;
import com.gianmarques.supporttracker.mapper.dto.person.PersonRequestDto;
import com.gianmarques.supporttracker.mapper.dto.person.PersonResponseDto;
import org.modelmapper.ModelMapper;

import java.util.List;


public class PersonMapper {



    public static PersonResponseDto toDto(Person person) {
        return new ModelMapper().map(person, PersonResponseDto.class);
    }

    public static List<PersonResponseDto> toListDTO(List<Person> persons) {
        return persons.stream().map(person -> toDto(person)).toList();
    }

}
