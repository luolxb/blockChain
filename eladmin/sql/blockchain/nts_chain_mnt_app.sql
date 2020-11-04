create table mnt_app
(
    app_id        bigint auto_increment comment 'ID'
        primary key,
    name          varchar(255)  null comment '应用名称',
    upload_path   varchar(255)  null comment '上传目录',
    deploy_path   varchar(255)  null comment '部署路径',
    backup_path   varchar(255)  null comment '备份路径',
    port          int(255)      null comment '应用端口',
    start_script  varchar(4000) null comment '启动脚本',
    deploy_script varchar(4000) null comment '部署脚本',
    create_by     varchar(255)  null comment '创建者',
    update_by     varchar(255)  null comment '更新者',
    create_time   datetime      null comment '创建日期',
    update_time   datetime      null comment '更新时间'
)
    comment '应用管理' charset = utf8;

