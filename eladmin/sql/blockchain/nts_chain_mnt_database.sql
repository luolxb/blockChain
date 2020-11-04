create table mnt_database
(
    db_id       varchar(50)  not null comment 'ID'
        primary key,
    name        varchar(255) not null comment '名称',
    jdbc_url    varchar(255) not null comment 'jdbc连接',
    user_name   varchar(255) not null comment '账号',
    pwd         varchar(255) not null comment '密码',
    create_by   varchar(255) null comment '创建者',
    update_by   varchar(255) null comment '更新者',
    create_time datetime     null comment '创建时间',
    update_time datetime     null comment '更新时间'
)
    comment '数据库管理' charset = utf8;

