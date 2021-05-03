create table if not exists migrations_pgk
(
    id                bigserial,
    version           int          not null,
    description       varchar(255) not null,
    installed_on      timestamp    not null default now(),
    execution_time_ms bigint       not null,

    primary key (id)
);
