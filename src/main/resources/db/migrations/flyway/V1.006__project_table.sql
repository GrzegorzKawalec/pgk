create table if not exists pgk_project
(
    id                 bigserial,
    project_manager_id int     not null,
    name               varchar not null,
    date_start         date    not null,
    date_end           date    not null,
    is_active          boolean not null default true,
    description        text    not null,

    created_on         timestamp,
    created_by         int,
    last_modified_on   timestamp,
    last_modified_by   int,
    entity_version     int,

    primary key (id),
    foreign key (project_manager_id) references pgk_user,

    foreign key (created_by) references pgk_user,
    foreign key (last_modified_by) references pgk_user
);

create index if not exists pgk_project_name_idx on pgk_project (name);
create index if not exists pgk_project_date_start_idx on pgk_project (date_start);
create index if not exists pgk_project_date_end_idx on pgk_project (date_end);
create index if not exists pgk_project_is_active_idx on pgk_project (is_active);
create index if not exists pgk_project_description_idx on pgk_project (description);

create table if not exists pgk_project_aud
(
    rev                bigint not null,
    revtype            smallint,

    id                 bigint,
    project_manager_id int,
    name               varchar,
    date_start         date,
    date_end           date,
    is_active          boolean,
    description        text,

    created_on         timestamp,
    created_by         int,
    last_modified_on   timestamp,
    last_modified_by   int,
    entity_version     int,

    primary key (id, rev),
    foreign key (rev) references revinfo
);

create table if not exists pgk_project_participant_link
(
    project_id       bigint not null,
    user_id          int    not null,

    created_on       timestamp,
    created_by       int,
    last_modified_on timestamp,
    last_modified_by int,
    entity_version   int,

    primary key (project_id, user_id),
    foreign key (project_id) references pgk_project,
    foreign key (user_id) references pgk_user,

    foreign key (created_by) references pgk_user,
    foreign key (last_modified_by) references pgk_user
);

create table if not exists pgk_project_participant_link_aud
(
    rev              bigint not null,
    revtype          smallint,

    project_id       bigint,
    user_id          bigint,

    created_on       timestamp,
    created_by       int,
    last_modified_on timestamp,
    last_modified_by int,
    entity_version   int,

    primary key (project_id, user_id, rev),
    foreign key (rev) references revinfo
);

create table if not exists pgk_project_legal_act_link
(
    project_id       bigint not null,
    legal_act_id     bigint not null,

    created_on       timestamp,
    created_by       int,
    last_modified_on timestamp,
    last_modified_by int,
    entity_version   int,

    primary key (project_id, legal_act_id),
    foreign key (project_id) references pgk_project,
    foreign key (legal_act_id) references pgk_legal_act,

    foreign key (created_by) references pgk_user,
    foreign key (last_modified_by) references pgk_user
);

create table if not exists pgk_project_legal_act_link_aud
(
    rev              bigint not null,
    revtype          smallint,

    project_id       bigint,
    legal_act_id     bigint,

    created_on       timestamp,
    created_by       int,
    last_modified_on timestamp,
    last_modified_by int,
    entity_version   int,

    primary key (project_id, legal_act_id, rev),
    foreign key (rev) references revinfo
);
