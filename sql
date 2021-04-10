create database enrollctrl;
use enrollctrl;

create table answer
(
    problemId        int          not null,
    jsonVal          varchar(255) null,
    author_id        int          not null,
    questionnaire_id int          null
);

create table answer_author
(
    author_id    int auto_increment
        primary key,
    author_name  varchar(50) null,
    author_email varchar(80) null
);

create table department
(
    id         int          not null,
    name       varchar(50)  not null,
    `describe` varchar(255) null,
    constraint department_id_uindex
        unique (id)
);

alter table department
    add primary key (id);

create table problem
(
    id              int auto_increment
        primary key,
    title           varchar(255) not null,
    type            varchar(50)  not null,
    jsonVal         varchar(255) null,
    `index`         int          not null,
    questionnaireId int          not null
);

create table questionnaire
(
    id           int auto_increment,
    departmentId int          not null,
    title        varchar(128) not null,
    `describe`   varchar(255) null,
    constraint questionnaire_id_uindex
        unique (id)
);

alter table questionnaire
    add primary key (id);

create table user
(
    username     varchar(50)  not null,
    password     varchar(128) not null,
    email        varchar(100) not null,
    gender       bit          not null,
    isManager    bit          not null,
    departmentId int          not null,
    registerDate datetime     not null,
    `describe`   varchar(255) null,
    constraint user_username_uindex
        unique (username)
);

alter table user
    add primary key (username);

