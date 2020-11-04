create table sys_dict_detail
(
    detail_id   bigint auto_increment comment 'ID'
        primary key,
    dict_id     bigint(11)   null comment '字典id',
    label       varchar(255) not null comment '字典标签',
    value       varchar(255) not null comment '字典值',
    dict_sort   int(5)       null comment '排序',
    create_by   varchar(255) null comment '创建者',
    update_by   varchar(255) null comment '更新者',
    create_time datetime     null comment '创建日期',
    update_time datetime     null comment '更新时间'
)
    comment '数据字典详情' charset = utf8;

create index FK5tpkputc6d9nboxojdbgnpmyb
    on sys_dict_detail (dict_id);

INSERT INTO nts_chain.sys_dict_detail (detail_id, dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) VALUES (1, 1, '激活', 'true', 1, null, null, '2019-10-27 20:31:36', null);
INSERT INTO nts_chain.sys_dict_detail (detail_id, dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) VALUES (2, 1, '禁用', 'false', 2, null, null, null, null);
INSERT INTO nts_chain.sys_dict_detail (detail_id, dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) VALUES (3, 4, '启用', 'true', 1, null, null, null, null);
INSERT INTO nts_chain.sys_dict_detail (detail_id, dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) VALUES (4, 4, '停用', 'false', 2, null, null, '2019-10-27 20:31:36', null);
INSERT INTO nts_chain.sys_dict_detail (detail_id, dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) VALUES (5, 5, '启用', 'true', 1, null, null, null, null);
INSERT INTO nts_chain.sys_dict_detail (detail_id, dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) VALUES (6, 5, '停用', 'false', 2, null, null, '2019-10-27 20:31:36', null);