create table mnt_deploy_history
(
    history_id  varchar(50)  not null comment 'ID'
        primary key,
    app_name    varchar(255) not null comment '应用名称',
    deploy_date datetime     not null comment '部署日期',
    deploy_user varchar(50)  not null comment '部署用户',
    ip          varchar(20)  not null comment '服务器IP',
    deploy_id   bigint       null comment '部署编号'
)
    comment '部署历史管理' charset = utf8;

