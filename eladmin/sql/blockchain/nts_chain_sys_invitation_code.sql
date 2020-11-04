create table sys_invitation_code
(
    id          bigint auto_increment
        primary key,
    code        varchar(16)          null comment '邀请码',
    create_time datetime             null,
    create_by   varchar(32)          null,
    update_time datetime             null,
    update_by   varchar(32)          null,
    is_use      tinyint(1) default 1 null comment '是否使用 1：没有，2 已使用'
)
    charset = utf8;

create index index_code
    on sys_invitation_code (code);

INSERT INTO nts_chain.sys_invitation_code (id, code, create_time, create_by, update_time, update_by, is_use) VALUES (1, '61563885', '2020-07-17 09:13:38', 'admin', null, null, 1);
INSERT INTO nts_chain.sys_invitation_code (id, code, create_time, create_by, update_time, update_by, is_use) VALUES (2, '94622830', '2020-07-17 09:13:45', 'admin', null, null, 1);
INSERT INTO nts_chain.sys_invitation_code (id, code, create_time, create_by, update_time, update_by, is_use) VALUES (3, 'Cch7yLcR', '2020-07-17 09:16:07', 'admin', null, null, 1);
INSERT INTO nts_chain.sys_invitation_code (id, code, create_time, create_by, update_time, update_by, is_use) VALUES (4, 'mUTewc5A', '2020-07-17 09:16:16', 'admin', null, null, 2);
INSERT INTO nts_chain.sys_invitation_code (id, code, create_time, create_by, update_time, update_by, is_use) VALUES (5, 'HuqxGtR2', '2020-07-17 14:39:48', 'admin', null, null, 2);
INSERT INTO nts_chain.sys_invitation_code (id, code, create_time, create_by, update_time, update_by, is_use) VALUES (6, 'HkE1mGaI', '2020-07-18 09:23:20', 'admin', null, null, 2);
INSERT INTO nts_chain.sys_invitation_code (id, code, create_time, create_by, update_time, update_by, is_use) VALUES (7, 'jXcZ0OVV', '2020-07-21 09:49:57', 'admin', null, null, 2);
INSERT INTO nts_chain.sys_invitation_code (id, code, create_time, create_by, update_time, update_by, is_use) VALUES (8, 'Kz1IVzel', '2020-07-21 14:03:53', '999', null, null, 2);
INSERT INTO nts_chain.sys_invitation_code (id, code, create_time, create_by, update_time, update_by, is_use) VALUES (9, 'd4IDFMSM', '2020-07-22 08:42:56', 'admin', null, null, 1);
INSERT INTO nts_chain.sys_invitation_code (id, code, create_time, create_by, update_time, update_by, is_use) VALUES (10, 'TY1pWLof', '2020-07-22 11:18:30', 'admin', null, null, 2);
INSERT INTO nts_chain.sys_invitation_code (id, code, create_time, create_by, update_time, update_by, is_use) VALUES (11, 'OeWN6sRb', '2020-07-24 18:11:22', 'admin', null, null, 1);
INSERT INTO nts_chain.sys_invitation_code (id, code, create_time, create_by, update_time, update_by, is_use) VALUES (12, 'ZgnOnVLZ', '2020-07-24 18:11:34', 'admin', null, null, 2);
INSERT INTO nts_chain.sys_invitation_code (id, code, create_time, create_by, update_time, update_by, is_use) VALUES (13, 'xBGbaRxU', '2020-07-27 14:06:32', 'admin', null, null, 2);
INSERT INTO nts_chain.sys_invitation_code (id, code, create_time, create_by, update_time, update_by, is_use) VALUES (14, 'qfUVwWG4', '2020-07-27 14:08:33', 'admin', null, null, 2);
INSERT INTO nts_chain.sys_invitation_code (id, code, create_time, create_by, update_time, update_by, is_use) VALUES (15, 'b5DduMM4', '2020-07-27 14:13:02', 'admin', null, null, 2);
INSERT INTO nts_chain.sys_invitation_code (id, code, create_time, create_by, update_time, update_by, is_use) VALUES (16, 'lhGNLiKL', '2020-07-27 17:37:11', 'admin', null, null, 2);
INSERT INTO nts_chain.sys_invitation_code (id, code, create_time, create_by, update_time, update_by, is_use) VALUES (17, '550P3zVs', '2020-07-28 09:20:10', 'admin', null, null, 2);
INSERT INTO nts_chain.sys_invitation_code (id, code, create_time, create_by, update_time, update_by, is_use) VALUES (18, 'hBcImmO1', '2020-07-28 09:57:04', 'admin', null, null, 2);
INSERT INTO nts_chain.sys_invitation_code (id, code, create_time, create_by, update_time, update_by, is_use) VALUES (19, 'p6oU1Aa5', '2020-07-28 11:20:42', 'admin', null, null, 2);
INSERT INTO nts_chain.sys_invitation_code (id, code, create_time, create_by, update_time, update_by, is_use) VALUES (20, 'AUCd7Rd0', '2020-07-28 15:47:13', 'admin', null, null, 2);
INSERT INTO nts_chain.sys_invitation_code (id, code, create_time, create_by, update_time, update_by, is_use) VALUES (21, 'PpPXsQKa', '2020-07-29 11:23:30', 'admin', null, null, 2);
INSERT INTO nts_chain.sys_invitation_code (id, code, create_time, create_by, update_time, update_by, is_use) VALUES (22, 'aN3CAU7R', '2020-07-30 15:19:29', '系统', null, null, 1);
INSERT INTO nts_chain.sys_invitation_code (id, code, create_time, create_by, update_time, update_by, is_use) VALUES (23, 'buzELPvQ', '2020-07-31 10:05:10', 'admin', null, null, 2);
INSERT INTO nts_chain.sys_invitation_code (id, code, create_time, create_by, update_time, update_by, is_use) VALUES (24, 'yrCJOJx6', '2020-07-31 10:05:14', 'admin', null, null, 2);
INSERT INTO nts_chain.sys_invitation_code (id, code, create_time, create_by, update_time, update_by, is_use) VALUES (25, 'hhMOgLuh', '2020-08-07 16:55:17', 'admin', null, null, 2);
INSERT INTO nts_chain.sys_invitation_code (id, code, create_time, create_by, update_time, update_by, is_use) VALUES (26, 'fuAPMcZ2', '2020-08-18 10:33:57', 'admin', null, null, 2);
INSERT INTO nts_chain.sys_invitation_code (id, code, create_time, create_by, update_time, update_by, is_use) VALUES (27, '11YwdLvx', '2020-08-20 16:40:34', 'admin', null, null, 2);
INSERT INTO nts_chain.sys_invitation_code (id, code, create_time, create_by, update_time, update_by, is_use) VALUES (28, 'vrwgud0O', '2020-08-21 11:30:04', 'admin', null, null, 2);