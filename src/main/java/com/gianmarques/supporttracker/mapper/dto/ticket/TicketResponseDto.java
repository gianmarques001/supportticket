package com.gianmarques.supporttracker.mapper.dto.ticket;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class TicketResponseDto {

    private Long id;

    private String status;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDate;


    public TicketResponseDto() {
    }

    public TicketResponseDto(Long id, String status, LocalDateTime createdDate) {
        this.id = id;
        this.status = status;
        this.createdDate = createdDate;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }


}
