create table sys_job
(
    job_id      bigint auto_increment comment 'ID'
        primary key,
    name        varchar(255) not null comment '岗位名称',
    enabled     bit          not null comment '岗位状态',
    job_sort    int(5)       null comment '排序',
    create_by   varchar(255) null comment '创建者',
    update_by   varchar(255) null comment '更新者',
    create_time datetime     null comment '创建日期',
    update_time datetime     null comment '更新时间',
    constraint uniq_name
        unique (name)
)
    comment '岗位' charset = utf8;

create index inx_enabled
    on sys_job (enabled);

INSERT INTO nts_chain.sys_job (job_id, name, enabled, job_sort, create_by, update_by, create_time, update_time) VALUES (8, '人事专员', true, 3, null, null, '2019-03-29 14:52:28', null);
INSERT INTO nts_chain.sys_job (job_id, name, enabled, job_sort, create_by, update_by, create_time, update_time) VALUES (10, '产品经理', true, 4, null, null, '2019-03-29 14:55:51', null);
INSERT INTO nts_chain.sys_job (job_id, name, enabled, job_sort, create_by, update_by, create_time, update_time) VALUES (11, '全栈开发', true, 2, null, 'admin', '2019-03-31 13:39:30', '2020-05-05 11:33:43');
INSERT INTO nts_chain.sys_job (job_id, name, enabled, job_sort, create_by, update_by, create_time, update_time) VALUES (12, '软件测试', true, 5, null, 'admin', '2019-03-31 13:39:43', '2020-05-10 19:56:26');