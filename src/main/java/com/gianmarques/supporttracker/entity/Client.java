package com.gianmarques.supporttracker.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "tb_clients")
public class Client extends Person {



    @OneToMany(mappedBy = "client")
    private List<Ticket> tickets;


    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}
