CREATE
DATABASE IF NOT EXISTS `demo`
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE
`demo`;

create table organization
(
    id          int auto_increment
        primary key,
    name        varchar(100)                       not null comment '组织名称',
    description text null comment '组织描述',
    created_at  datetime default CURRENT_TIMESTAMP not null,
    updated_at  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint index_name
        unique (name)
) comment '组织信息表';

create table employee
(
    id              int auto_increment
        primary key,
    organization_id int                                not null comment '所属组织ID',
    name            varchar(50)                        not null comment '员工姓名',
    email           varchar(100)                       not null comment '员工邮箱',
    phone           varchar(20) null comment '联系电话',
    position        varchar(50) null comment '职位',
    status          tinyint(1) default 1                 not null comment '状态：1-在职，0-离职',
    created_at      datetime default CURRENT_TIMESTAMP not null,
    updated_at      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint idx_1
        unique (organization_id, name)
) comment '员工信息表';

create index idx_organization
    on employee (organization_id);

create table permission
(
    id          int auto_increment
        primary key,
    code        varchar(50)                        not null comment '权限代码',
    name        varchar(50)                        not null comment '权限名称',
    description text null comment '权限描述',
    created_at  datetime default CURRENT_TIMESTAMP not null,
    updated_at  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint idx_code
        unique (code)
) comment '权限表';

create table employee_permission
(
    id            int auto_increment
        primary key,
    employee_id   int                                 not null comment '员工ID',
    permission_id int                                 not null comment '权限ID',
    created_at    timestamp default CURRENT_TIMESTAMP not null,
    constraint idx_employee_permission
        unique (employee_id, permission_id)
) comment '员工权限关联表';

create index idx_permission
    on employee_permission (permission_id);

create table project
(
    id              int auto_increment
        primary key,
    organization_id int                                not null comment '所属组织ID',
    created_by      int                                not null comment '创建人ID',
    name            varchar(100)                       not null comment '项目名称',
    description     text null comment '项目描述',
    status          tinyint  default 0                 not null comment '状态：-1删除，0未开始，1进行中，1已完成',
    start_date      date null comment '开始日期',
    end_date        date null comment '结束日期',
    created_at      datetime default CURRENT_TIMESTAMP not null,
    updated_at      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
) comment '项目表';

create index idx_organization
    on project (organization_id);

create index idx_status
    on project (status);

create table task
(
    id          int auto_increment
        primary key,
    project_id  int                                not null comment '所属项目ID',
    name        varchar(100)                       not null comment '任务名称',
    description text null comment '任务描述',
    progress    tinyint  default 0                 not null comment '进度百分比(0-100)',
    start_time  datetime null comment '开始时间',
    time_spent  int      default 0 null comment '完成耗时(分钟)',
    assignee_id int null comment '负责人ID',
    status      tinyint(1) default 0                 not null comment '状态：-1-删除，0-未开始，1-调研阶段，2-立项阶段，3-设计阶段，4-验证阶段，5-完成',
    created_at  datetime default CURRENT_TIMESTAMP not null,
    updated_at  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    version     int      default 0                 not null comment '版本号'
) comment '任务表';

create index idx_assignee
    on task (assignee_id);

create index idx_project
    on task (project_id);

create index idx_status
    on task (status);

create table task_employee
(
    id          int auto_increment
        primary key,
    task_id     int                                not null comment '任务ID',
    employee_id int                                not null comment '员工ID',
    role        tinyint(1) default 0                 not null comment '角色：0-参与者，1-关注者',
    created_at  datetime default CURRENT_TIMESTAMP not null,
    status      int null comment '状态：0正常，1删除',
    constraint idx_task_employee
        unique (task_id, employee_id)
) comment '任务关联员工表';

create index idx_employee
    on task_employee (employee_id);

create table task_log
(
    id          int auto_increment
        primary key,
    task_id     int                                not null comment '任务ID',
    employee_id int                                not null comment '操作人ID',
    action      int                                not null comment '操作类型 0新建，1调研，2立项，3设计，4验证，5完成',
    created_at  datetime default CURRENT_TIMESTAMP not null
) comment '任务操作日志表';

create index idx_employee
    on task_log (employee_id);

create index idx_task
    on task_log (task_id);


----------
-- 初始化数据
INSERT INTO demo.organization (id, name)
VALUES (1, '公司1');

INSERT INTO demo.permission (id, code, name, description)
VALUES (1, '0000', 'admin', 'admin');

INSERT INTO demo.employee (id, organization_id, name, email)
VALUES (1, 1, 'staff1', 'staff1@org.dev.com');
INSERT INTO demo.employee (id, organization_id, name, email)
VALUES (2, 1, 'staff2', 'staff2@org.dev.com');

INSERT INTO demo.employee_permission (id, employee_id, permission_id)
VALUES (1, 1, 1);

INSERT INTO demo.project (id, organization_id, created_by, name)
VALUES (1, 1, 1, '测试');
