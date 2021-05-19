create table if not exists audit_request
(
    id                      bigserial,

    user_id                 integer,
    req_uri                 text,
    req_method              text,
    req_param               text,
    req_body                text,
    response                text,
    error_uuid              varchar(36),
    error_type              varchar,
    error_message           text,
    process_duration_millis bigint,
    audit_duration_millis   bigint,

    primary key (id)
);
