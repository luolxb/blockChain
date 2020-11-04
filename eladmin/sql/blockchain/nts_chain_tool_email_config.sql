create table tool_email_config
(
    config_id bigint       not null comment 'ID'
        primary key,
    from_user varchar(255) null comment '收件人',
    host      varchar(255) null comment '邮件服务器SMTP地址',
    pass      varchar(255) null comment '密码',
    port      varchar(255) null comment '端口',
    user      varchar(255) null comment '发件者用户名'
)
    comment '邮箱配置' charset = utf8;

