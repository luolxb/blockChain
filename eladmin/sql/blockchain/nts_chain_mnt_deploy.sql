create table mnt_deploy
(
    deploy_id   bigint auto_increment comment 'ID'
        primary key,
    app_id      bigint       null comment '应用编号',
    create_by   varchar(255) null comment '创建者',
    update_by   varchar(255) null comment '更新者',
    create_time datetime     null,
    update_time datetime     null comment '更新时间'
)
    comment '部署管理' charset = utf8;

create index FK6sy157pseoxx4fmcqr1vnvvhy
    on mnt_deploy (app_id);

