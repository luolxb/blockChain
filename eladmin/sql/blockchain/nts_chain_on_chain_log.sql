create table on_chain_log
(
    id                     bigint auto_increment
        primary key,
    deposit_certificate_id bigint      null comment '存证id',
    message                text        null comment '发送信息',
    create_by              varchar(32) null,
    create_time            datetime    null,
    update_by              varchar(32) null,
    update_time            date        null
);

