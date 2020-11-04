create table company_token
(
    id                         bigint auto_increment
        primary key,
    company_id                 bigint      null comment '企业ID',
    deposit_certificate_id     bigint      null comment '存证ID',
    transaction_time           datetime    null comment '交易时间',
    transaction_type           tinyint(1)  null comment '交易类型 1：买入； 2：卖出',
    transaction_wallet_address varchar(64) null comment '钱包地址',
    create_time                datetime    null,
    create_by                  varchar(32) null,
    update_time                datetime    null,
    update_by                  varchar(32) null
);

create index index_company_id
    on company_token (company_id);

