create table sys_dict
(
    dict_id     bigint auto_increment comment 'ID'
        primary key,
    name        varchar(255) not null comment '字典名称',
    description varchar(255) null comment '描述',
    create_by   varchar(255) null comment '创建者',
    update_by   varchar(255) null comment '更新者',
    create_time datetime     null comment '创建日期',
    update_time datetime     null comment '更新时间'
)
    comment '数据字典' charset = utf8;

INSERT INTO nts_chain.sys_dict (dict_id, name, description, create_by, update_by, create_time, update_time) VALUES (1, 'user_status', '用户状态', null, null, '2019-10-27 20:31:36', null);
INSERT INTO nts_chain.sys_dict (dict_id, name, description, create_by, update_by, create_time, update_time) VALUES (4, 'dept_status', '部门状态', null, null, '2019-10-27 20:31:36', null);
INSERT INTO nts_chain.sys_dict (dict_id, name, description, create_by, update_by, create_time, update_time) VALUES (5, 'job_status', '岗位状态', null, null, '2019-10-27 20:31:36', null);