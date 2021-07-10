create table if not exists pgk_legal_act
(
    id               bigserial,
    name             varchar not null,
    date_of          date    not null,
    link             varchar not null,
    description      text,

    created_on       timestamp,
    created_by       int,
    last_modified_on timestamp,
    last_modified_by int,
    entity_version   int,

    unique (link),
    unique (name, date_of),
    primary key (id),

    foreign key (created_by) references pgk_user,
    foreign key (last_modified_by) references pgk_user
);

create index if not exists pgk_legal_act_name_idx on pgk_legal_act (name);
create index if not exists pgk_legal_act_date_of_idx on pgk_legal_act (date_of);
create index if not exists pgk_legal_act_link_idx on pgk_legal_act (link);

create table if not exists pgk_legal_act_aud
(
    rev              bigint not null,
    revtype          smallint,

    id               bigint,
    name             varchar,
    date_of          date,
    link             varchar,
    description      text,

    created_on       timestamp,
    created_by       int,
    last_modified_on timestamp,
    last_modified_by int,
    entity_version   int,

    primary key (id, rev),
    foreign key (rev) references revinfo
);
