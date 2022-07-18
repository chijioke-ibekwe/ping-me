create table chat_messages (
id                  bigserial not null,
message_text        varchar(255),
message_time        timestamp,
user_contact_id     int8,
primary key (id)
);

create table users (
id                  bigserial not null,
first_name          varchar(255),
last_name           varchar(255),
password            varchar(255),
salt                varchar(255),
username            varchar(255),
primary key (id)
);

create table user_contacts (
id                  bigserial not null,
contact_id          int8,
host_id             int8,
primary key (id)
);

alter table users add constraint uk_username unique (username);

alter table chat_messages add constraint fk_chat_messages_user_contacts foreign key (user_contact_id) references user_contacts;

alter table user_contacts add constraint fk_user_contacts_users_1 foreign key (contact_id) references users;

alter table user_contacts add constraint fk_user_contacts_users_2 foreign key (host_id) references users;