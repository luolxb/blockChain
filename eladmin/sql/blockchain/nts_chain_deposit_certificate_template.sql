create table deposit_certificate_template
(
    id                 bigint auto_increment
        primary key,
    product_name       varchar(64)    null comment '产品名称',
    specification      varchar(64)    null comment '产品规格',
    case_number        varchar(32)    null comment '产品箱号',
    batch_number       varchar(64)    null comment '产品批次号',
    amount             bigint         null comment '产品数量',
    purchasing_company varbinary(255) null comment '采购企业',
    create_time        datetime       null,
    create_by          varchar(32)    null,
    update_time        datetime       null,
    update_by          varchar(32)    null,
    template_name      varchar(64)    null comment '模板名称',
    template_remark    varchar(255)   null comment '模板备注',
    param_type         varchar(64)    null comment '参数类型',
    param_name         varchar(255)   null comment '参数名称',
    parameter          varchar(1400)  null comment '扩充参数',
    certificate_logo   bigint         null comment '存证Logo',
    fragrance          varchar(64)    null comment '香型',
    degree             varchar(64)    null comment '度数',
    capacity           varchar(64)    null comment '容量'
);

INSERT INTO nts_chain.deposit_certificate_template (id, product_name, specification, case_number, batch_number, amount, purchasing_company, create_time, create_by, update_time, update_by, template_name, template_remark, param_type, param_name, parameter, certificate_logo, fragrance, degree, capacity) VALUES (1, 'string', 'string', 'string', 'string', 0, 0x737472696E67, '2020-07-08 17:31:51', 'admin', null, null, 'string', 'string', 'string', 'string', null, null, null, null, null);
INSERT INTO nts_chain.deposit_certificate_template (id, product_name, specification, case_number, batch_number, amount, purchasing_company, create_time, create_by, update_time, update_by, template_name, template_remark, param_type, param_name, parameter, certificate_logo, fragrance, degree, capacity) VALUES (2, 'string', 'string', 'string', 'string', 0, 0x737472696E67, '2020-07-09 17:24:42', 'admin', null, null, '产品', 'string', 'string', 'string', null, null, null, null, null);