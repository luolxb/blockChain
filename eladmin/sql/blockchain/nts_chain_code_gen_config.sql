create table code_gen_config
(
    config_id   bigint auto_increment comment 'ID'
        primary key,
    table_name  varchar(255) null comment '表名',
    author      varchar(255) null comment '作者',
    cover       bit          null comment '是否覆盖',
    module_name varchar(255) null comment '模块名称',
    pack        varchar(255) null comment '至于哪个包下',
    path        varchar(255) null comment '前端代码生成的路径',
    api_path    varchar(255) null comment '前端Api文件路径',
    prefix      varchar(255) null comment '表前缀',
    api_alias   varchar(255) null comment '接口名称'
)
    comment '代码生成器配置' charset = utf8;

create index idx_table_name
    on code_gen_config (table_name);

