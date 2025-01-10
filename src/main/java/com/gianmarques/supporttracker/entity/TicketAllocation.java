package com.gianmarques.supporttracker.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "tb_ticket_allocation")
public class TicketAllocation {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_ticket", nullable = false)
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "id_support", nullable = false)
    private Support support;


    public TicketAllocation() {
    }

    public TicketAllocation(Long id, Ticket ticket, Client client, Support support) {
        this.id = id;
        this.ticket = ticket;
        this.client = client;
        this.support = support;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Support getSupport() {
        return support;
    }

    public void setSupport(Support support) {
        this.support = support;
    }
}
