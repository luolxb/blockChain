create table new_dict
(
    id          bigint auto_increment
        primary key,
    code        varchar(32)          null comment '字典code',
    value       varchar(64)          null comment '值',
    description varchar(255)         null comment '描述',
    remark      varbinary(255)       null comment '备注',
    p_id        bigint               null comment '父节点',
    create_time datetime             not null comment '创建时间',
    update_time datetime             null comment '修改时间',
    create_by   varchar(64)          null comment '创建人',
    update_by   varchar(64)          null comment '修改人',
    enabled     tinyint(1) default 1 null comment '是否启用 1：启用；2：禁用',
    del_flag    tinyint(1) default 1 null comment '是否删除1：否；2：是'
);

INSERT INTO nts_chain.new_dict (id, code, value, description, remark, p_id, create_time, update_time, create_by, update_by, enabled, del_flag) VALUES (1, 'company_type_01', '企业类型', null, null, 0, '2020-07-08 11:20:27', null, 'admin', null, 1, 1);
INSERT INTO nts_chain.new_dict (id, code, value, description, remark, p_id, create_time, update_time, create_by, update_by, enabled, del_flag) VALUES (2, 'company_type_001', '生产厂商', null, null, 1, '2020-07-08 11:21:21', null, 'admin', null, 1, 1);
INSERT INTO nts_chain.new_dict (id, code, value, description, remark, p_id, create_time, update_time, create_by, update_by, enabled, del_flag) VALUES (3, 'company_type_002', '经销商', null, null, 1, '2020-07-08 11:21:41', null, 'admin', null, 1, 1);