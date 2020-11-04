create table sys_users_jobs
(
    user_id bigint not null comment '用户ID',
    job_id  bigint not null comment '岗位ID',
    primary key (user_id, job_id)
)
    charset = utf8;

INSERT INTO nts_chain.sys_users_jobs (user_id, job_id) VALUES (1, 11);
INSERT INTO nts_chain.sys_users_jobs (user_id, job_id) VALUES (1, 12);
INSERT INTO nts_chain.sys_users_jobs (user_id, job_id) VALUES (2, 12);