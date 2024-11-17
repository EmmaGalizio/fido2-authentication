
grant select on performance_schema.* to 'mysql.session'@localhost;

grant trigger on sys.* to 'mysql.sys'@localhost;

grant audit_abort_exempt, firewall_exempt, select, system_user on *.* to 'mysql.infoschema'@localhost;

grant audit_abort_exempt, authentication_policy_admin, backup_admin, clone_admin, connection_admin, firewall_exempt, persist_ro_variables_admin, session_variables_admin, shutdown, super, system_user, system_variables_admin on *.* to 'mysql.session'@localhost;

grant audit_abort_exempt, firewall_exempt, system_user on *.* to 'mysql.sys'@localhost;

grant allow_nonexistent_definer, alter, alter routine, application_password_admin, audit_abort_exempt, audit_admin, authentication_policy_admin, backup_admin, binlog_admin, binlog_encryption_admin, clone_admin, connection_admin, create, create role, create routine, create tablespace, create temporary tables, create user, create view, delete, drop, drop role, encryption_key_admin, event, execute, file, firewall_exempt, flush_optimizer_costs, flush_privileges, flush_status, flush_tables, flush_user_resources, group_replication_admin, group_replication_stream, index, innodb_redo_log_archive, innodb_redo_log_enable, insert, lock tables, optimize_local_table, passwordless_user_admin, persist_ro_variables_admin, process, references, reload, replication client, replication slave, replication_applier, replication_slave_admin, resource_group_admin, resource_group_user, role_admin, select, sensitive_variables_observer, service_connection_admin, session_variables_admin, set_any_definer, show databases, show view, show_routine, shutdown, super, system_user, system_variables_admin, table_encryption_admin, telemetry_log_admin, transaction_gtid_tag, trigger, update, xa_recover_admin, grant option on *.* to root;

grant allow_nonexistent_definer, alter, alter routine, application_password_admin, audit_abort_exempt, audit_admin, authentication_policy_admin, backup_admin, binlog_admin, binlog_encryption_admin, clone_admin, connection_admin, create, create role, create routine, create tablespace, create temporary tables, create user, create view, delete, drop, drop role, encryption_key_admin, event, execute, file, firewall_exempt, flush_optimizer_costs, flush_privileges, flush_status, flush_tables, flush_user_resources, group_replication_admin, group_replication_stream, index, innodb_redo_log_archive, innodb_redo_log_enable, insert, lock tables, optimize_local_table, passwordless_user_admin, persist_ro_variables_admin, process, references, reload, replication client, replication slave, replication_applier, replication_slave_admin, resource_group_admin, resource_group_user, role_admin, select, sensitive_variables_observer, service_connection_admin, session_variables_admin, set_any_definer, show databases, show view, show_routine, shutdown, super, system_user, system_variables_admin, table_encryption_admin, telemetry_log_admin, transaction_gtid_tag, trigger, update, xa_recover_admin, grant option on *.* to root@localhost;

CREATE SCHEMA IF NOT EXISTS `db_fido2` DEFAULT CHARACTER SET utf8 ;
USE `db_fido2` ;

create table metadata
(
    id                         bigint auto_increment
        primary key,
    aaguid                     varchar(255) null,
    biometric_status_reports   text         null,
    content                    text         not null,
    status_reports             text         null,
    time_of_last_status_change varchar(255) null
);

create table metadata_toc
(
    id                           bigint auto_increment
        primary key,
    no                           bigint       not null,
    encoded_metadata_toc_payload text         not null,
    legal_header                 text         null,
    metadata_source              text         not null,
    next_update                  varchar(255) not null
);

create table metadata_yubico
(
    id      int auto_increment
        primary key,
    content text not null
);

create table rp
(
    description varchar(255) null,
    icon        varchar(255) null,
    id          varchar(255) not null
        primary key,
    name        varchar(255) not null
);

create table user
(
    email      varchar(255) null,
    first_name varchar(255) null,
    id         varchar(255) not null
        primary key,
    last_name  varchar(255) null,
    username   varchar(255) null
);

create table user_key
(
    attestation_type        tinyint      null,
    cred_protect            int          null,
    rk                      bit          null,
    signature_algorithm     int          not null,
    authenticated_timestamp datetime(6)  null,
    id                      bigint auto_increment
        primary key,
    registered_timestamp    datetime(6)  null,
    sign_counter            bigint       null,
    aaguid                  varchar(36)  not null,
    user_display_name       varchar(64)  not null,
    username                varchar(64)  not null,
    user_icon               varchar(128) null,
    user_id                 varchar(128) not null,
    credential_id           varchar(256) not null,
    public_key              text         not null,
    rp_entity_id            varchar(255) null,
    constraint FKn8lic9ekst6j4tmy9vs36we8p
        foreign key (rp_entity_id) references rp (id)
);

create table authenticator_transport
(
    id          bigint auto_increment
        primary key,
    user_key_id bigint       not null,
    transport   varchar(255) null,
    constraint FKr5ng3evn4q5h581vo2ndet463
        foreign key (user_key_id) references user_key (id)
);

