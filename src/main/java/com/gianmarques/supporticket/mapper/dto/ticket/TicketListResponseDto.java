package com.gianmarques.supporticket.mapper.dto.ticket;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class TicketListResponseDto {

    private Long id;

    private String status;

    private String title;

    private String clientName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDate;

    public TicketListResponseDto() {
    }

    public TicketListResponseDto(Long id, String status, String clientName, String title, LocalDateTime createdDate) {
        this.id = id;
        this.status = status;
        this.title = title;
        this.clientName = clientName;
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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
