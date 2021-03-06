create table namespace
(
    id          varchar(255) not null,
    created_on  timestamp,
    description varchar(255),
    name        varchar(255),
    owner       varchar(255),
    version     int4,
    primary key (id)
);

create table company
(
    id                             varchar(255) not null,
    created_on                     timestamp,
    description                    varchar(255),
    name                           varchar(255),
    ruc                            varchar(11),
    sunat_password                 varchar(255),
    sunat_username                 varchar(255),
    sunat_url_factura              varchar(255),
    sunat_url_guia_remision        varchar(255),
    sunat_url_percepcion_retencion varchar(255),
    version                        int4,
    namespace_id                   varchar(255) not null,
    primary key (id)
);

create table ubl_document
(
    id                             varchar(255) not null,
    created_on                     timestamp,
    in_progress                    char(1),
    document_id                    varchar(255),
    document_type                  varchar(255),
    retries                        int4,
    ruc                            varchar(255),
    storage_cdr                    varchar(255),
    storage_file                   varchar(255),
    sunat_code                     int4,
    sunat_description              varchar(255),
    sunat_status                   varchar(255),
    sunat_ticket                   varchar(255),
    file_valid                     char(1),
    file_validation_error          varchar(255),
    error                          varchar(255),
    voided_line_document_type_code varchar(255),
    will_retry_on                  timestamp,
    namespace_id                   varchar(255),
    primary key (id)
);

create table ubl_document_sunat_notes
(
    ubl_document_id varchar(255) not null,
    value           varchar(255)
);

create table ubl_document_event
(
    id          varchar(255) not null,
    created_on  timestamp,
    description varchar(255),
    status      varchar(255),
    document_id varchar(255),
    primary key (id)
);


alter table if exists namespace drop
    constraint if exists UKrf676d3s4bqqyh8dud0uv1gof;

alter table if exists namespace
    add constraint UKrf676d3s4bqqyh8dud0uv1gof unique (name);


alter table if exists company drop
    constraint if exists company_ns_unique;

alter table if exists company
    add constraint company_ns_unique unique (namespace_id, ruc);

alter table if exists company
    add constraint company_ns_fk
    foreign key (namespace_id)
    references namespace on
delete
cascade;


alter table if exists ubl_document
    add constraint FKci8icuh34c4vjwkyj81tihv5r
    foreign key (namespace_id)
    references namespace on
delete
cascade;


alter table if exists ubl_document_sunat_notes
    add constraint FK6x9142wv16xao4un5xxgu60by
    foreign key (ubl_document_id)
    references ubl_document on
delete
cascade;


alter table if exists ubl_document_event
    add constraint FKhkjjk98wgev9l7vlccl8kg7yq
    foreign key (document_id)
    references ubl_document on
delete
cascade;
