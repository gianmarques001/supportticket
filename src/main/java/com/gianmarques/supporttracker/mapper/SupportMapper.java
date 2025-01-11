package com.gianmarques.supporttracker.mapper;

import com.gianmarques.supporttracker.entity.Support;
import com.gianmarques.supporttracker.entity.Ticket;
import com.gianmarques.supporttracker.mapper.dto.person.PersonRequestDto;
import com.gianmarques.supporttracker.mapper.dto.person.PersonResponseDto;
import com.gianmarques.supporttracker.mapper.dto.support.SupportListResponseDto;
import org.modelmapper.ModelMapper;

import java.util.List;

public class SupportMapper {

    public static Support toSupport(PersonRequestDto personRequestDto) {
        return new ModelMapper().map(personRequestDto, Support.class);
    }

    public static PersonResponseDto toDto(Support support) {
        return new ModelMapper().map(support, PersonResponseDto.class);
    }

    public static List<SupportListResponseDto> toList(List<Support> supports) {
        return supports.stream().map(support -> toListDto(support)).toList();
    }

    private static SupportListResponseDto toListDto(Support support) {
        SupportListResponseDto supportListResponseDto = new SupportListResponseDto();

        for (Ticket ticket : support.getTickets()) {
            supportListResponseDto.setTitle(ticket.getTitle());
            supportListResponseDto.setDescription(ticket.getDescription());
            supportListResponseDto.setStatus(ticket.getStatus().toString());
            supportListResponseDto.setClientName(ticket.getClient().getName());
            supportListResponseDto.setDateCreated(ticket.getCreatedDate());
            supportListResponseDto.setDateFinished(ticket.getModifiedDate());
        }

        return supportListResponseDto;

    }


}
