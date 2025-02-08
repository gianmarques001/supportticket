package com.gianmarques.supporttracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "tb_supports")
public class Support extends Person {

    @JsonIgnore
    @OneToMany(mappedBy = "support")
    private List<Ticket> tickets;


    public List<Ticket> getTickets() {
        return tickets;
    }


}
