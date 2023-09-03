drop database if exists csms;
create database if not exists csms;
use csms;

create table customer
(
    customerId      varchar(4)  not null
        primary key,
    name            varchar(50) null,
    address         text        null,
    nic             varchar(12) null,
    telephoneNumber varchar(12) null,
    email           varchar(45) null
);

create table employee
(
    employeeId      varchar(4)  not null
        primary key,
    name            varchar(50) null,
    address         text        null,
    nic             varchar(12) null,
    telephoneNumber varchar(12) null,
    `rank`          varchar(25) null
);

create table item
(
    itemCode    varchar(4)    not null
        primary key,
    itemType    varchar(25)   null,
    description text          null,
    unitPrice   decimal(8, 2) null,
    qtyOnStock  int           null
);

create table repair
(
    repairId    varchar(4)  not null,
    receiveDate date        null,
    returnDate  date        null,
    status      varchar(15) null,
    description text        null,
    customerId  varchar(4)  not null,
    primary key (repairId, customerId),
    constraint repair_ibfk_1
        foreign key (customerId) references customer (customerId)
            on update cascade on delete cascade
);

create table supplier
(
    supplierId      varchar(4)  not null
        primary key,
    name            varchar(50) null,
    address         text        null,
    telephoneNumber varchar(12) null,
    email           varchar(45) null
);

create table supplies
(
    suppliesId varchar(4) not null,
    date       date       null,
    time       time       null,
    supplierId varchar(4) not null,
    primary key (suppliesId, supplierId),
    constraint supplies_ibfk_1
        foreign key (supplierId) references supplier (supplierId)
            on update cascade on delete cascade
);

create index supplierId
    on supplies (supplierId);

create table suppliesdetails
(
    suppliesId varchar(4)    not null,
    itemCode   varchar(4)    not null,
    quantity   int           null,
    unitPrice  decimal(8, 2) null,
    primary key (suppliesId, itemCode),
    constraint suppliesdetails_ibfk_1
        foreign key (itemCode) references item (itemCode)
            on update cascade on delete cascade,
    constraint suppliesdetails_ibfk_2
        foreign key (suppliesId) references supplies (suppliesId)
            on update cascade on delete cascade
);

create index itemCode
    on suppliesdetails (itemCode);

create table transaction
(
    transactionId varchar(4)  not null,
    date          date        null,
    time          time        null,
    customerId    varchar(4)  not null,
    type          varchar(25) null,
    primary key (transactionId, customerId),
    constraint transaction_ibfk_1
        foreign key (customerId) references customer (customerId)
            on update cascade on delete cascade
);

create table itemtransactiondetails
(
    transactionId varchar(4)    not null,
    itemCode      varchar(4)    not null,
    quantity      int           null,
    unitPrice     decimal(8, 2) null,
    primary key (transactionId, itemCode),
    constraint itemtransactiondetails_ibfk_1
        foreign key (itemCode) references item (itemCode)
            on update cascade on delete cascade,
    constraint itemtransactiondetails_ibfk_2
        foreign key (transactionId) references transaction (transactionId)
            on update cascade on delete cascade
);

create index itemCode
    on itemtransactiondetails (itemCode);

create table repairtransactiondetails
(
    transactionId varchar(4)    not null,
    repairId      varchar(4)    not null,
    repairPrice   decimal(8, 2) null,
    primary key (transactionId, repairId),
    constraint repairtransactiondetails_ibfk_1
        foreign key (repairId) references repair (repairId)
            on update cascade on delete cascade,
    constraint repairtransactiondetails_ibfk_2
        foreign key (transactionId) references transaction (transactionId)
            on update cascade on delete cascade
);

create index repairId
    on repairtransactiondetails (repairId);

create index customerId
    on transaction (customerId);

create table user
(
    userId          varchar(4)  not null
        primary key,
    userName        varchar(50) null,
    userPassword    varchar(25) null,
    nic             varchar(12) null,
    telephoneNumber varchar(12) null,
    email           varchar(45) null,
    `rank`          varchar(25) null
);

create table loginrecord
(
    loginId varchar(4) not null,
    date    date       null,
    time    time       null,
    userId  varchar(4) not null,
    primary key (loginId, userId),
    constraint loginrecord_ibfk_1
        foreign key (userId) references user (userId)
            on update cascade on delete cascade
);

create index userId
    on loginrecord (userId);

