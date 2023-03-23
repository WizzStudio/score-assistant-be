create table if not exists lesson_plan
(
    plan_id   int auto_increment
        primary key,
    plan_code varchar(255) null,
    grade     int          null,
    plan_name varchar(100) null,
    plan_data varchar(255) null
)
    charset = utf8mb4;

create table if not exists lessons
(
    lesson_id         varchar(50)  not null
        primary key,
    lesson_name       varchar(100) null,
    lesson_type       varchar(50)  null,
    lesson_group_name varchar(255) null,
    lesson_group_code varchar(255) null,
    grade_score       varchar(50)  null,
    check_type        varchar(50)  null
)
    charset = utf8mb4;

create table if not exists users
(
    user_id    bigint       not null
        primary key,
    password   varchar(255) not null,
    score_data varchar(255) null
)
    charset = utf8;

