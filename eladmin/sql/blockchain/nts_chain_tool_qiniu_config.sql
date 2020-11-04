create table tool_qiniu_config
(
    config_id  bigint       not null comment 'ID'
        primary key,
    access_key text         null comment 'accessKey',
    bucket     varchar(255) null comment 'Bucket 识别符',
    host       varchar(255) not null comment '外链域名',
    secret_key text         null comment 'secretKey',
    type       varchar(255) null comment '空间类型',
    zone       varchar(255) null comment '机房'
)
    comment '七牛云配置' charset = utf8;

