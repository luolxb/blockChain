create table tool_qiniu_content
(
    content_id  bigint auto_increment comment 'ID'
        primary key,
    bucket      varchar(255) null comment 'Bucket 识别符',
    name        varchar(255) null comment '文件名称',
    size        varchar(255) null comment '文件大小',
    type        varchar(255) null comment '文件类型：私有或公开',
    url         varchar(255) null comment '文件url',
    suffix      varchar(255) null comment '文件后缀',
    update_time datetime     null comment '上传或同步的时间',
    constraint uniq_name
        unique (name)
)
    comment '七牛云文件存储' charset = utf8;

