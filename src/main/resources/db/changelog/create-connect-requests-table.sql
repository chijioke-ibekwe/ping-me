create table connect_requests (
id                  bigserial not null,
sender_id           int8,
recipient_id        int8,
request_status      varchar(255),
primary key (id)
);

alter table user_contacts drop column request_status;

alter table connect_requests add constraint fk_connect_requests_users_1 foreign key (sender_id) references users;

alter table connect_requests add constraint fk_connect_requests_users_2 foreign key (recipient_id) references users;