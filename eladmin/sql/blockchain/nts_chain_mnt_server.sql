create table mnt_server
(
    server_id   bigint auto_increment comment 'ID'
        primary key,
    account     varchar(50)  null comment '账号',
    ip          varchar(20)  null comment 'IP地址',
    name        varchar(100) null comment '名称',
    password    varchar(100) null comment '密码',
    port        int          null comment '端口',
    create_by   varchar(255) null comment '创建者',
    update_by   varchar(255) null comment '更新者',
    create_time datetime     null comment '创建时间',
    update_time datetime     null comment '更新时间'
)
    comment '服务器管理' charset = utf8;

create index idx_ip
    on mnt_server (ip);

INSERT INTO nts_chain.mnt_server (server_id, account, ip, name, password, port, create_by, update_by, create_time, update_time) VALUES (1, 'root', '132.232.129.20', '腾讯云', 'Dqjdda1996.', 8013, null, null, '2019-11-24 20:35:02', null);