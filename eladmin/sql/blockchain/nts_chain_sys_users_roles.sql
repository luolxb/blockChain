create table sys_users_roles
(
    user_id bigint not null comment '用户ID',
    role_id bigint not null comment '角色ID',
    primary key (user_id, role_id)
)
    comment '用户角色关联' charset = utf8;

create index FKq4eq273l04bpu4efj0jd0jb98
    on sys_users_roles (role_id);

INSERT INTO nts_chain.sys_users_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO nts_chain.sys_users_roles (user_id, role_id) VALUES (16, 4);
INSERT INTO nts_chain.sys_users_roles (user_id, role_id) VALUES (84, 5);
INSERT INTO nts_chain.sys_users_roles (user_id, role_id) VALUES (91, 7);
INSERT INTO nts_chain.sys_users_roles (user_id, role_id) VALUES (99, 18);
INSERT INTO nts_chain.sys_users_roles (user_id, role_id) VALUES (101, 19);
INSERT INTO nts_chain.sys_users_roles (user_id, role_id) VALUES (103, 20);
INSERT INTO nts_chain.sys_users_roles (user_id, role_id) VALUES (104, 21);
INSERT INTO nts_chain.sys_users_roles (user_id, role_id) VALUES (199, 21);
INSERT INTO nts_chain.sys_users_roles (user_id, role_id) VALUES (105, 22);
INSERT INTO nts_chain.sys_users_roles (user_id, role_id) VALUES (109, 24);