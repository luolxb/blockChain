create table mnt_deploy_server
(
    deploy_id bigint not null comment '部署ID',
    server_id bigint not null comment '服务ID',
    primary key (deploy_id, server_id)
)
    comment '应用与服务器关联' charset = utf8;

create index FKeaaha7jew9a02b3bk9ghols53
    on mnt_deploy_server (server_id);

