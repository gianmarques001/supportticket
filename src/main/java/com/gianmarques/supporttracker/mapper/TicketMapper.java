package com.gianmarques.supporttracker.mapper;

import com.gianmarques.supporttracker.entity.Ticket;
import com.gianmarques.supporttracker.mapper.dto.ticket.TicketClientResponseDto;
import com.gianmarques.supporttracker.mapper.dto.ticket.TicketListResponseDto;
import com.gianmarques.supporttracker.mapper.dto.ticket.TicketRequestDto;
import com.gianmarques.supporttracker.mapper.dto.ticket.TicketResponseDto;
import org.modelmapper.ModelMapper;

import java.util.List;

public class TicketMapper {

    public static Ticket toTicket(TicketRequestDto ticketRequestDto) {
        return new ModelMapper().map(ticketRequestDto, Ticket.class);
    }

    public static TicketResponseDto toDto(Ticket ticket) {
        return new ModelMapper().map(ticket, TicketResponseDto.class);
    }

    private static TicketListResponseDto toListDto(Ticket ticket) {
        return new ModelMapper().map(ticket, TicketListResponseDto.class);
    }

    public static List<TicketListResponseDto> toList(List<Ticket> tickets) {
        return tickets.stream().map(ticket -> toListDto(ticket)).toList();

    }


    private static TicketClientResponseDto toTicketClientDto(Ticket ticket) {
        return new ModelMapper().map(ticket, TicketClientResponseDto.class);

    }


    public static List<TicketClientResponseDto> toListClient(List<Ticket> tickets) {
        return tickets.stream().map(ticket -> toTicketClientDto(ticket)).toList();
    }
}
