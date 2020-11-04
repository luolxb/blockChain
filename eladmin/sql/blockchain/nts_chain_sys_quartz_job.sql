create table sys_quartz_job
(
    job_id              bigint auto_increment comment 'ID'
        primary key,
    bean_name           varchar(255) null comment 'Spring Bean名称',
    cron_expression     varchar(255) null comment 'cron 表达式',
    is_pause            bit          null comment '状态：1暂停、0启用',
    job_name            varchar(255) null comment '任务名称',
    method_name         varchar(255) null comment '方法名称',
    params              varchar(255) null comment '参数',
    description         varchar(255) null comment '备注',
    person_in_charge    varchar(100) null comment '负责人',
    email               varchar(100) null comment '报警邮箱',
    sub_task            varchar(100) null comment '子任务ID',
    pause_after_failure bit          null comment '任务失败后是否暂停',
    create_by           varchar(255) null comment '创建者',
    update_by           varchar(255) null comment '更新者',
    create_time         datetime     null comment '创建日期',
    update_time         datetime     null comment '更新时间'
)
    comment '定时任务' charset = utf8;

create index inx_is_pause
    on sys_quartz_job (is_pause);

INSERT INTO nts_chain.sys_quartz_job (job_id, bean_name, cron_expression, is_pause, job_name, method_name, params, description, person_in_charge, email, sub_task, pause_after_failure, create_by, update_by, create_time, update_time) VALUES (2, 'testTask', '0/5 * * * * ?', true, '测试1', 'run1', 'test', '带参测试，多参使用json', '测试', null, null, null, null, 'admin', '2019-08-22 14:08:29', '2020-05-05 17:26:19');
INSERT INTO nts_chain.sys_quartz_job (job_id, bean_name, cron_expression, is_pause, job_name, method_name, params, description, person_in_charge, email, sub_task, pause_after_failure, create_by, update_by, create_time, update_time) VALUES (3, 'testTask', '0/5 * * * * ?', true, '测试', 'run', '', '不带参测试', 'Zheng Jie', '', '2,6', true, null, 'admin', '2019-09-26 16:44:39', '2020-05-05 20:45:39');
INSERT INTO nts_chain.sys_quartz_job (job_id, bean_name, cron_expression, is_pause, job_name, method_name, params, description, person_in_charge, email, sub_task, pause_after_failure, create_by, update_by, create_time, update_time) VALUES (5, 'Test', '0/5 * * * * ?', true, '任务告警测试', 'run', null, '测试', 'test', '', null, true, 'admin', 'admin', '2020-05-05 20:32:41', '2020-05-05 20:36:13');
INSERT INTO nts_chain.sys_quartz_job (job_id, bean_name, cron_expression, is_pause, job_name, method_name, params, description, person_in_charge, email, sub_task, pause_after_failure, create_by, update_by, create_time, update_time) VALUES (6, 'testTask', '0/5 * * * * ?', true, '测试3', 'run2', null, '测试3', 'Zheng Jie', '', null, true, 'admin', 'admin', '2020-05-05 20:35:41', '2020-05-05 20:36:07');