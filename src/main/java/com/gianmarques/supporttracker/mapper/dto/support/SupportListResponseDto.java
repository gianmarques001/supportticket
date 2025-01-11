package com.gianmarques.supporttracker.mapper.dto.support;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class SupportListResponseDto {

    private String title;

    private String description;

    private String clientName;

    private String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateCreated;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateFinished;


    public SupportListResponseDto() {
    }

    public SupportListResponseDto(String title, String description, String clientName, String status, LocalDateTime dateCreated, LocalDateTime dateFinished) {
        this.title = title;
        this.description = description;
        this.clientName = clientName;
        this.status = status;
        this.dateCreated = dateCreated;
        this.dateFinished = dateFinished;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDateTime getDateFinished() {
        return dateFinished;
    }

    public void setDateFinished(LocalDateTime dateFinished) {
        this.dateFinished = dateFinished;
    }
}

