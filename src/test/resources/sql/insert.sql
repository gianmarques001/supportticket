insert into tb_persons(id, name, email, password, role)
values (100, 'Admin', 'user01@admin.com', '$2a$12$Cbqx1xRfm/kEOwjyt64rR.3sakFGvfcSPWcckHyQeA4QbPwjjjuCa', 'ROLE_ADMIN');


insert into tb_persons(id, name, email, password, role)
values (200, 'Tony Stark', 'tony@test.com', '$2a$12$Cbqx1xRfm/kEOwjyt64rR.3sakFGvfcSPWcckHyQeA4QbPwjjjuCa',
        'ROLE_CLIENT');
insert into tb_clients (id)
values (200);

insert into tb_persons(id, name, email, password, role)
values (201, 'Steve Rogers', 'steve@test.com', '$2a$12$Cbqx1xRfm/kEOwjyt64rR.3sakFGvfcSPWcckHyQeA4QbPwjjjuCa',
        'ROLE_CLIENT');
insert into tb_clients (id)
values (201);

insert into tb_persons(id, name, email, password, role)
values (300, 'Peter Parker', 'peter@test.com', '$2a$12$Cbqx1xRfm/kEOwjyt64rR.3sakFGvfcSPWcckHyQeA4QbPwjjjuCa',
        'ROLE_SUPPORT');
insert into tb_supports (id)
values (300);

insert into tb_persons(id, name, email, password, role)
values (301, 'Bruce Banner', 'bruce@test.com', '$2a$12$Cbqx1xRfm/kEOwjyt64rR.3sakFGvfcSPWcckHyQeA4QbPwjjjuCa',
        'ROLE_SUPPORT');
insert into tb_supports (id)
values (301);


insert into tb_tickets(id, title, description, status, client_id, created_date, modified_date)
values (110, 'Error on Screen', 'Error notebook screen', 'OPEN', 201, '2025-01-01 11:44:39.764116',
        '2025-01-01 11:44:39.764116');


insert into tb_ticket_allocation(id_client, id_support, id_ticket)
values (201, 301, 110);

update tb_tickets
set support_id = 301
where id = 110;
