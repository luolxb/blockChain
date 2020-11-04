create table tool_picture
(
    picture_id  bigint auto_increment comment 'ID'
        primary key,
    filename    varchar(255) null comment '图片名称',
    md5code     varchar(255) null comment '文件的MD5值',
    size        varchar(255) null comment '图片大小',
    url         varchar(255) null comment '图片地址',
    delete_url  varchar(255) null comment '删除的URL',
    height      varchar(255) null comment '图片高度',
    width       varchar(255) null comment '图片宽度',
    username    varchar(255) null comment '用户名称',
    create_time datetime     null comment '上传日期',
    constraint uniq_md5_code
        unique (md5code)
)
    comment 'Sm.Ms图床' charset = utf8;

create index inx_url
    on tool_picture (url);

