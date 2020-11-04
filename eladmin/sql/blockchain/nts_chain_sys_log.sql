create table sys_log
(
    log_id           bigint auto_increment comment 'ID'
        primary key,
    description      varchar(255) null,
    log_type         varchar(255) null,
    method           varchar(255) null,
    params           text         null,
    request_ip       varchar(255) null,
    time             bigint       null,
    username         varchar(255) null,
    address          varchar(255) null,
    browser          varchar(255) null,
    exception_detail text         null,
    create_time      datetime     null
)
    comment '系统日志' charset = utf8;

create index inx_log_type
    on sys_log (log_type);

create index log_create_time_index
    on sys_log (create_time);
