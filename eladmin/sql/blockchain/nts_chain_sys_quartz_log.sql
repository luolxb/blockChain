create table sys_quartz_log
(
    log_id           bigint auto_increment comment 'ID'
        primary key,
    bean_name        varchar(255) null,
    create_time      datetime     null,
    cron_expression  varchar(255) null,
    exception_detail text         null,
    is_success       bit          null,
    job_name         varchar(255) null,
    method_name      varchar(255) null,
    params           varchar(255) null,
    time             bigint       null
)
    comment '定时任务日志' charset = utf8;

