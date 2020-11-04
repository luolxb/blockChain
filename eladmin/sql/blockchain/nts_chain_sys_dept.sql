create table sys_dept
(
    dept_id     bigint auto_increment comment 'ID'
        primary key,
    pid         bigint             null comment '上级部门',
    sub_count   int(5) default 0   null comment '子部门数目',
    name        varchar(255)       not null comment '名称',
    dept_sort   int(5) default 999 null comment '排序',
    enabled     bit                not null comment '状态',
    create_by   varchar(255)       null comment '创建者',
    update_by   varchar(255)       null comment '更新者',
    create_time datetime           null comment '创建日期',
    update_time datetime           null comment '更新时间'
)
    comment '部门' charset = utf8;

create index inx_enabled
    on sys_dept (enabled);

create index inx_pid
    on sys_dept (pid);

INSERT INTO nts_chain.sys_dept (dept_id, pid, sub_count, name, dept_sort, enabled, create_by, update_by, create_time, update_time) VALUES (2, 7, 0, '研发部', 3, true, null, 'admin', '2019-03-25 09:15:32', '2020-05-10 17:37:58');
INSERT INTO nts_chain.sys_dept (dept_id, pid, sub_count, name, dept_sort, enabled, create_by, update_by, create_time, update_time) VALUES (5, 7, 0, '运维部', 4, true, null, null, '2019-03-25 09:20:44', null);
INSERT INTO nts_chain.sys_dept (dept_id, pid, sub_count, name, dept_sort, enabled, create_by, update_by, create_time, update_time) VALUES (6, 8, 0, '测试部', 6, true, null, null, '2019-03-25 09:52:18', null);
INSERT INTO nts_chain.sys_dept (dept_id, pid, sub_count, name, dept_sort, enabled, create_by, update_by, create_time, update_time) VALUES (7, null, 2, '华南分部', 0, true, null, 'admin', '2019-03-25 11:04:50', '2020-05-10 19:59:12');
INSERT INTO nts_chain.sys_dept (dept_id, pid, sub_count, name, dept_sort, enabled, create_by, update_by, create_time, update_time) VALUES (8, null, 2, '华北分部', 1, true, null, 'admin', '2019-03-25 11:04:53', '2020-05-14 12:54:00');
INSERT INTO nts_chain.sys_dept (dept_id, pid, sub_count, name, dept_sort, enabled, create_by, update_by, create_time, update_time) VALUES (15, 8, 0, 'UI部门', 7, true, 'admin', 'admin', '2020-05-13 22:56:53', '2020-05-14 12:54:13');