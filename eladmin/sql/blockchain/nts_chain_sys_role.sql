create table sys_role
(
    role_id     bigint auto_increment comment 'ID'
        primary key,
    name        varchar(255) not null comment '名称',
    level       int(255)     null comment '角色级别',
    description varchar(255) null comment '描述',
    data_scope  varchar(255) null comment '数据权限',
    create_by   varchar(255) null comment '创建者',
    update_by   varchar(255) null comment '更新者',
    create_time datetime     null comment '创建日期',
    update_time datetime     null comment '更新时间',
    constraint uniq_name
        unique (name)
)
    comment '角色表' charset = utf8;

create index role_name_index
    on sys_role (name);

INSERT INTO nts_chain.sys_role (role_id, name, level, description, data_scope, create_by, update_by, create_time, update_time) VALUES (1, '超级管理员', 3, null, '本级', null, 'admin', '2018-11-23 11:04:37', '2020-07-29 13:35:20');
INSERT INTO nts_chain.sys_role (role_id, name, level, description, data_scope, create_by, update_by, create_time, update_time) VALUES (2, '操作员', null, null, null, null, null, null, null);
INSERT INTO nts_chain.sys_role (role_id, name, level, description, data_scope, create_by, update_by, create_time, update_time) VALUES (4, '管理员', 3, null, '本级', 'admin', 'admin', '2020-07-28 18:12:13', '2020-07-28 18:12:13');
INSERT INTO nts_chain.sys_role (role_id, name, level, description, data_scope, create_by, update_by, create_time, update_time) VALUES (5, '测试', 3, null, '本级', 'admin', 'admin', '2020-07-29 08:58:28', '2020-07-29 09:41:30');
INSERT INTO nts_chain.sys_role (role_id, name, level, description, data_scope, create_by, update_by, create_time, update_time) VALUES (6, '145', 3, null, '本级', 'admin', 'test147', '2020-07-29 09:08:14', '2020-07-29 09:44:17');
INSERT INTO nts_chain.sys_role (role_id, name, level, description, data_scope, create_by, update_by, create_time, update_time) VALUES (7, '添加', 3, null, '本级', 'admin', 'admin', '2020-07-29 13:45:09', '2020-07-30 14:18:21');
INSERT INTO nts_chain.sys_role (role_id, name, level, description, data_scope, create_by, update_by, create_time, update_time) VALUES (8, '角色123', 3, null, '本级', 'admin', 'admin', '2020-07-29 18:18:50', '2020-07-29 18:18:50');
INSERT INTO nts_chain.sys_role (role_id, name, level, description, data_scope, create_by, update_by, create_time, update_time) VALUES (9, '角色2', 3, null, '本级', 'admin', 'admin', '2020-07-29 18:19:26', '2020-07-29 18:19:26');
INSERT INTO nts_chain.sys_role (role_id, name, level, description, data_scope, create_by, update_by, create_time, update_time) VALUES (10, '角色3', 3, null, '本级', 'admin', 'admin', '2020-07-29 18:20:47', '2020-07-29 18:20:47');
INSERT INTO nts_chain.sys_role (role_id, name, level, description, data_scope, create_by, update_by, create_time, update_time) VALUES (11, '角色914', 3, null, '本级', 'admin', 'admin', '2020-07-29 23:20:34', '2020-07-29 23:20:34');
INSERT INTO nts_chain.sys_role (role_id, name, level, description, data_scope, create_by, update_by, create_time, update_time) VALUES (12, '915', 3, null, '本级', 'admin', 'admin', '2020-07-29 23:23:59', '2020-07-30 08:52:19');
INSERT INTO nts_chain.sys_role (role_id, name, level, description, data_scope, create_by, update_by, create_time, update_time) VALUES (13, '1234', 3, null, '本级', 'admin', 'admin', '2020-07-30 08:53:00', '2020-07-30 08:53:59');
INSERT INTO nts_chain.sys_role (role_id, name, level, description, data_scope, create_by, update_by, create_time, update_time) VALUES (14, '系统管理员', 3, null, '本级', 'admin', 'admin', '2020-07-30 08:55:27', '2020-07-30 08:55:27');
INSERT INTO nts_chain.sys_role (role_id, name, level, description, data_scope, create_by, update_by, create_time, update_time) VALUES (16, '123', 3, null, '本级', 'add', 'admin', '2020-07-30 09:35:48', '2020-07-30 15:33:02');
INSERT INTO nts_chain.sys_role (role_id, name, level, description, data_scope, create_by, update_by, create_time, update_time) VALUES (17, '123456', 3, null, '本级', 'admin', 'admin', '2020-07-30 10:58:39', '2020-07-30 10:58:39');
INSERT INTO nts_chain.sys_role (role_id, name, level, description, data_scope, create_by, update_by, create_time, update_time) VALUES (18, '456465456', 3, null, '本级', 'admin', 'admin', '2020-07-30 11:11:28', '2020-07-30 15:32:40');
INSERT INTO nts_chain.sys_role (role_id, name, level, description, data_scope, create_by, update_by, create_time, update_time) VALUES (19, '商户', 3, null, '本级', 'add', 'add', '2020-07-30 14:09:37', '2020-07-30 14:09:37');
INSERT INTO nts_chain.sys_role (role_id, name, level, description, data_scope, create_by, update_by, create_time, update_time) VALUES (20, '存证', 3, null, '本级', 'admin', 'admin', '2020-07-30 14:15:22', '2020-07-30 14:15:22');
INSERT INTO nts_chain.sys_role (role_id, name, level, description, data_scope, create_by, update_by, create_time, update_time) VALUES (21, '通证', 3, null, '本级', 'admin', 'admin', '2020-07-30 14:38:31', '2020-07-30 14:38:31');
INSERT INTO nts_chain.sys_role (role_id, name, level, description, data_scope, create_by, update_by, create_time, update_time) VALUES (22, '系统', 3, null, '本级', 'admin', 'admin', '2020-07-30 15:03:16', '2020-07-30 15:03:16');
INSERT INTO nts_chain.sys_role (role_id, name, level, description, data_scope, create_by, update_by, create_time, update_time) VALUES (24, '325235', 3, null, '本级', 'admin', 'admin', '2020-07-31 09:52:51', '2020-07-31 09:52:51');