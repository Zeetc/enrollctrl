create database enrollctrl;
use enrollctrl;

create table enrollctrl.answer
(
    problemId        int auto_increment
        primary key,
    jsonVal          varchar(255) null,
    author_id        int          not null,
    questionnaire_id int          null
);

create table enrollctrl.answer_author
(
    author_id       int auto_increment
        primary key,
    author_name     varchar(50) null,
    author_email    varchar(80) null,
    is_pass         int         null,
    questionnaireId int         null
);

create table enrollctrl.department
(
    id              int auto_increment,
    department_name varchar(50)  not null,
    describes       varchar(255) null,
    constraint department_id_uindex
        unique (id)
);

alter table enrollctrl.department
    add primary key (id);

create table enrollctrl.permission
(
    permission_id   int         null,
    permission_name varchar(50) null
);

create table enrollctrl.problem
(
    id              int auto_increment
        primary key,
    title           varchar(255) not null,
    type            varchar(50)  not null,
    jsonVal         varchar(255) null,
    idx             int          not null,
    questionnaireId int          not null
);

create table enrollctrl.questionnaire
(
    id           int auto_increment
        primary key,
    departmentId int          not null,
    title        varchar(128) not null,
    describes    varchar(255) null,
    endDate      timestamp    null
);

create table enrollctrl.role
(
    role_id   int         null,
    role_name varchar(50) null
);

create table enrollctrl.role_permission
(
    role_id       int null,
    permission_id int null
);

create table enrollctrl.user
(
    username     varchar(50)  not null,
    password     varchar(128) not null,
    email        varchar(100) not null,
    gender       bit          not null,
    departmentId int          not null,
    registerDate datetime     not null,
    describes    varchar(255) null,
    constraint user_username_uindex
        unique (username)
);

alter table enrollctrl.user
    add primary key (username);

create table enrollctrl.user_role
(
    username varchar(50) null,
    role_id  int         null
);

