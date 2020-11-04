create table sys_roles_depts
(
    role_id bigint not null,
    dept_id bigint not null,
    primary key (role_id, dept_id)
)
    comment '角色部门关联' charset = utf8;

create index FK7qg6itn5ajdoa9h9o78v9ksur
    on sys_roles_depts (dept_id);

INSERT INTO nts_chain.sys_roles_depts (role_id, dept_id) VALUES (2, 7);