create table sys_menu
(
    menu_id     bigint auto_increment comment 'ID'
        primary key,
    pid         bigint              null comment '上级菜单ID',
    sub_count   int(5) default 0    null comment '子菜单数目',
    type        int                 null comment '菜单类型',
    title       varchar(255)        null comment '菜单标题',
    name        varchar(255)        null comment '组件名称',
    component   varchar(255)        null comment '组件',
    menu_sort   int(5)              null comment '排序',
    icon        varchar(255)        null comment '图标',
    path        varchar(255)        null comment '链接地址',
    i_frame     bit                 null comment '是否外链',
    cache       bit    default b'0' null comment '缓存',
    hidden      bit    default b'0' null comment '隐藏',
    permission  varchar(255)        null comment '权限',
    create_by   varchar(255)        null comment '创建者',
    update_by   varchar(255)        null comment '更新者',
    create_time datetime            null comment '创建日期',
    update_time datetime            null comment '更新时间'
)
    comment '系统菜单' charset = utf8;

create index inx_pid
    on sys_menu (pid);

create index uniq_name
    on sys_menu (name);

create index uniq_title
    on sys_menu (title);

INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (1, 0, 3, 2, '系统管理', 'system', '/api/s/users/page', 5, 'system', '/api/s/users', false, false, false, '', null, null, '2018-12-18 15:11:29', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (2, 1, 3, 2, '账号管理', 'User', '/api/s/users/page', 2, 'user', '/api/s/users/page', false, false, false, 'user:list', null, null, '2018-12-18 15:14:44', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (3, 1, 3, 2, '角色管理', 'Role', '/api/s/roles/page', 3, 'role', '/api/s/roles/page', false, false, false, 'roles:list', null, null, '2018-12-18 15:16:07', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (5, 1, 2, 2, '安全设置', 'Menu', '/api/s/company/user', 5, 'menu', '/api/s/company/user', false, false, false, 'company:user', null, null, '2018-12-18 15:17:28', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (44, 2, 0, 3, '添加', null, '/api/s/users', 2, '', '/api/s/users', false, false, false, 'user:add', null, null, '2019-10-29 10:59:46', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (45, 2, 0, 3, '编辑', null, '/api/s/users/update', 3, '', '/api/s/users/update', false, false, false, 'user:edit', null, null, '2019-10-29 11:00:08', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (46, 2, 0, 3, '删除', null, '/api/s/users/delete', 4, '', '/api/s/users/delete', false, false, false, 'user:del', null, null, '2019-10-29 11:00:23', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (48, 3, 0, 3, '添加', null, '/api/s/roles', 2, '', '/api/s/roles', false, false, false, 'roles:add', null, null, '2019-10-29 12:45:34', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (49, 3, 0, 3, '编辑', null, '/api/s/roles/update', 3, '', '/api/s/roles/update', false, false, false, 'roles:edit', null, null, '2019-10-29 12:46:16', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (50, 3, 0, 3, '删除', null, '/api/s/roles/dalete', 4, '', '/api/s/roles/dalete', false, false, false, 'roles:del', null, null, '2019-10-29 12:46:51', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (52, 5, 0, 3, '修改登录密码', null, '/api/s/users/updatePass', 2, '', '/api/s/users/updatePass', false, false, false, 'users:updatePass', null, null, '2019-10-29 12:55:07', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (53, 5, 0, 3, '修改手机号码', null, '/api/s/users/updatePhone', 3, '', '/api/s/users/updatePhone', false, false, false, 'users:updatePhone', null, null, '2019-10-29 12:55:40', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (54, 0, 7, 2, '商户管理', null, '/api/s/company/page', 1, null, '/api/s/company/page', false, false, false, '', null, null, '2020-07-18 14:51:52', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (55, 54, 0, 2, '商户列表', null, '/api/s/company/page', null, null, '/api/s/company/page', false, false, false, 'company:list', null, null, '2020-07-18 14:54:48', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (56, 55, 0, 3, '查询', null, '/api/s/company/page', null, null, '/api/s/company/page', false, false, false, 'company:list', null, null, '2020-07-18 14:54:51', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (57, 55, 0, 3, '添加', null, '/api/s/company/add', null, null, '/api/s/company/add', false, false, false, 'company:add', null, null, '2020-07-18 14:51:52', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (58, 55, 0, 3, '查看', null, '/api/s/company/detail', null, null, '/api/s/company/detail', false, false, false, 'company:detail', null, null, '2020-07-18 14:51:52', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (59, 55, 0, 3, '编辑', null, '/api/s/company/update', null, null, '/api/s/company/update', false, false, false, 'company:edit', null, null, '2020-07-18 14:51:52', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (60, 55, 0, 3, '删除', null, '/api/s/company/delete/{ids}', null, null, '/api/s/company/delete/{ids}', false, false, false, 'company:del', null, null, '2020-07-18 14:51:52', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (62, 0, 0, 2, '通证管理', null, '/api/s/token', 4, null, '/api/s/token/page', false, false, false, '', null, null, '2020-07-18 15:01:55', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (63, 62, 0, 2, '通证资产', null, '/api/s/token/page', null, null, '/api/s/token/page', false, false, false, 'token:list', null, null, '2020-07-18 14:51:52', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (64, 0, 4, 2, '存证管理', null, '/api/s/certificate/page', 2, null, '/api/s/certificate/page', false, false, false, '', null, null, '2020-07-18 14:51:52', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (65, 64, 0, 2, '存证列表', null, '/api/s/certificate/page', null, null, '/api/s/certificate', false, false, false, 'certificate:list', null, null, '2020-07-18 14:51:52', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (67, 65, 0, 3, '查看', null, '/api/s/certificate/detail', null, null, '/api/s/certificate/detail', false, false, false, 'certificate:detail', null, null, '2020-07-18 14:51:52', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (68, 65, 0, 3, '编辑', null, '/api/s/certificate/edit', null, null, '/api/s/certificate/edit', false, false, false, 'certificate:edit', null, null, '2020-07-18 14:51:52', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (69, 65, 0, 3, '审核', null, '/api/s/certificate/audit', null, null, '/api/s/certificate/audit', false, false, false, 'certificate:audit', null, null, '2020-07-18 14:51:52', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (70, 0, 4, 2, '存证模板管理', null, '/api/s/template/page', 3, null, '/api/s/template/page', false, false, false, '', null, null, '2020-07-18 14:51:52', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (72, 70, 0, 2, '存证模板列表', null, '/api/s/template/page', null, null, '/api/s/template/page', false, false, false, 'template:list', null, null, '2020-07-18 14:51:52', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (73, 72, 0, 3, '添加', null, '/api/s/template/add', null, null, '/api/s/template/add', false, false, false, 'template:add', null, null, '2020-07-18 14:51:52', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (74, 72, 0, 3, '编辑', null, '/api/s/template/edit', null, null, '/api/s/template/edit', false, false, false, 'template:edit', null, null, '2020-07-18 14:51:52', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (75, 72, 0, 3, '删除', null, '/api/s/template/del/{id}', null, null, '/api/s/template/del/{id}', false, false, false, 'template:del', null, null, '2020-07-18 14:51:52', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (76, 3, 0, 3, '查询', null, '/api/s/roles/page', null, null, '/api/s/roles/page', false, false, false, 'roles:list', null, null, '2020-07-18 15:46:55', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (77, 55, 0, 3, '审核', null, '/api/s/company/audit', null, null, '/api/s/company/audit', false, false, false, 'company:audit', null, null, '2020-07-18 15:52:50', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (78, 2, 0, 3, '查询', null, '/api/s/users/page', 5, null, '/api/s/users/page', false, false, false, 'users:list', null, null, '2020-07-18 16:02:47', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (79, 63, 0, 3, '导出', null, '/api/s/token/download', null, null, '/api/s/token/download', false, false, false, 'token:download', null, null, '2020-07-21 13:16:44', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (80, 55, 0, 3, '导出', null, '/api/s/company/download', null, null, '/api/s/company/download', false, false, false, 'company:download', null, null, '2020-07-21 13:18:26', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (81, 65, 0, 3, '导出', null, '/api/s/certificate/download', null, null, '/api/s/certificate/download', false, false, false, 'certificate:download', null, null, '2020-07-21 13:19:53', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (82, 63, 0, 3, '查询', null, '/api/s/token/page', null, null, '/api/s/token/page', false, false, false, 'token:list', null, null, '2020-07-21 13:16:44', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (83, 1, 0, 2, '邀请码管理', 'Cdoe', '/api/s/code/page', 6, null, '/api/s/code/page', false, false, false, 'code:list', null, null, '2018-12-18 15:17:28', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (84, 83, 0, 3, '查询', null, '/api/s/code/page', 1, null, '/api/s/code/page', false, false, false, 'code:list', null, null, '2018-12-18 15:17:28', null);
INSERT INTO nts_chain.sys_menu (menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time) VALUES (85, 83, 0, 3, '生成邀请码', null, '/api/s/code/generate', null, null, '/api/s/code/generate', false, false, false, 'code:generate', null, null, '2018-12-18 15:17:28', null);