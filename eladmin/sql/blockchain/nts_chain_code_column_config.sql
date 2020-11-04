create table code_column_config
(
    column_id       bigint auto_increment comment 'ID'
        primary key,
    table_name      varchar(255) null,
    column_name     varchar(255) null,
    column_type     varchar(255) null,
    dict_name       varchar(255) null,
    extra           varchar(255) null,
    form_show       bit          null,
    form_type       varchar(255) null,
    key_type        varchar(255) null,
    list_show       bit          null,
    not_null        bit          null,
    query_type      varchar(255) null,
    remark          varchar(255) null,
    date_annotation varchar(255) null
)
    comment '代码生成字段信息存储' charset = utf8;

create index idx_table_name
    on code_column_config (table_name);

