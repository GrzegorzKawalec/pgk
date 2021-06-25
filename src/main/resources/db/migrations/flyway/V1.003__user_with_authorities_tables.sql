--   user   --

create table if not exists pgk_user
(
    id               serial,
    email            varchar(100) not null,
    first_name       varchar(50)  not null,
    last_name        varchar(50)  not null,
    phone_number     varchar(20),
    description      text,
    is_active        boolean      not null default true,

    created_on       timestamp,
    created_by       int,
    last_modified_on timestamp,
    last_modified_by int,
    entity_version   int,

    unique (email),
    primary key (id),

    foreign key (created_by) references pgk_user,
    foreign key (last_modified_by) references pgk_user
);

create index if not exists pgk_user_email_idx on pgk_user (email);

create table if not exists pgk_user_aud
(
    rev              bigint not null,
    revtype          smallint,

    id               int,
    email            varchar(100),
    first_name       varchar(50),
    last_name        varchar(50),
    phone_number     varchar(20),
    description      text,
    is_active        boolean,

    created_on       timestamp,
    created_by       int,
    last_modified_on timestamp,
    last_modified_by int,
    entity_version   int,

    primary key (id, rev),
    foreign key (rev) references revinfo
);

--   authority   --

create table if not exists pgk_authority
(
    id               smallint,
    authority        varchar(25) not null,

    created_on       timestamp,
    created_by       int,
    last_modified_on timestamp,
    last_modified_by int,
    entity_version   int,

    unique (authority),
    primary key (id),

    foreign key (created_by) references pgk_user,
    foreign key (last_modified_by) references pgk_user
);

create table if not exists pgk_authority_aud
(
    rev              bigint not null,
    revtype          smallint,

    id               smallint,
    authority        varchar(25),

    created_on       timestamp,
    created_by       int,
    last_modified_on timestamp,
    last_modified_by int,
    entity_version   int,

    primary key (id, rev),
    foreign key (rev) references revinfo
);

--   role   --

create table if not exists pgk_role
(
    id               serial,
    name             varchar not null,
    description      text,

    created_on       timestamp,
    created_by       int,
    last_modified_on timestamp,
    last_modified_by int,
    entity_version   int,

    unique (name),
    primary key (id),

    foreign key (created_by) references pgk_user,
    foreign key (last_modified_by) references pgk_user
);

create table if not exists pgk_role_aud
(
    rev              bigint not null,
    revtype          smallint,

    id               int,
    name             varchar,
    description      text,

    created_on       timestamp,
    created_by       int,
    last_modified_on timestamp,
    last_modified_by int,
    entity_version   int,

    primary key (id, rev),
    foreign key (rev) references revinfo
);

--   role and authority link table   --

create table if not exists pgk_role_authority_link
(
    role_id          int      not null,
    authority_id     smallint not null,

    created_on       timestamp,
    created_by       int,
    last_modified_on timestamp,
    last_modified_by int,
    entity_version   int,

    primary key (role_id, authority_id),
    foreign key (role_id) references pgk_role,
    foreign key (authority_id) references pgk_authority,

    foreign key (created_by) references pgk_user,
    foreign key (last_modified_by) references pgk_user
);

create table if not exists pgk_role_authority_link_aud
(
    rev              bigint not null,
    revtype          smallint,

    role_id          int,
    authority_id     smallint,

    created_on       timestamp,
    created_by       int,
    last_modified_on timestamp,
    last_modified_by int,
    entity_version   int,

    primary key (role_id, authority_id, rev),
    foreign key (rev) references revinfo
);

--   user credentials   --

create table if not exists pgk_user_credentials
(
    email            varchar,
    password         varchar not null,
    role_id          int     not null,
    is_active        boolean not null default true,

    created_on       timestamp,
    created_by       int,
    last_modified_on timestamp,
    last_modified_by int,
    entity_version   int,

    primary key (email),
    foreign key (role_id) references pgk_role,
    foreign key (email) references pgk_user (email),

    foreign key (created_by) references pgk_user,
    foreign key (last_modified_by) references pgk_user
);

create index if not exists pgk_user_credentials_email_idx on pgk_user_credentials (email);

create table if not exists pgk_user_credentials_aud
(
    rev              bigint not null,
    revtype          smallint,

    email            varchar,
    password         varchar,
    role_id          int,
    is_active        boolean,

    created_on       timestamp,
    created_by       int,
    last_modified_on timestamp,
    last_modified_by int,
    entity_version   int,

    primary key (email, rev),
    foreign key (rev) references revinfo
);
