package com.gianmarques.supporticket.mapper.dto.ticket;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TicketRequestDto {

    @NotNull
    @Size(min = 10, max = 30)
    private String title;

    @NotNull
    @Size(min = 10, max = 50)
    private String description;

    public TicketRequestDto() {
    }

    public TicketRequestDto(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public @NotNull @Size(min = 10, max = 15) String getTitle() {
        return title;
    }

    public void setTitle(@NotNull @Size(min = 10, max = 15) String title) {
        this.title = title;
    }

    public @NotNull @Size(min = 10, max = 50) String getDescription() {
        return description;
    }

    public void setDescription(@NotNull @Size(min = 10, max = 50) String description) {
        this.description = description;
    }
}
