create database JWC collate utf8mb4_bin;
use JWC;
# 管理员表
create table admin
(
    id       int         not null primary key auto_increment comment 'ID',
    name     varchar(50) not null unique comment '用户名',
    password varchar(50) not null comment '密码'
);
insert into admin value (0, 'admin', 'admin');
# 院系表
create table faculty
(
    id   int auto_increment not null primary key comment 'ID',
    name varchar(30) unique not null comment '院系名'
);
# 职称表
create table title
(
    id   int auto_increment not null primary key comment 'ID',
    name varchar(30) unique not null comment '职称名'
);

# 教室表
create table room
(
    id   int auto_increment not null primary key comment 'ID',
    name varchar(30) unique not null comment '教室名'
);
# 教师表
create table teacher
(
    id       int primary key not null auto_increment comment 'ID',
    avatar   varchar(50) comment '头像',
    name     varchar(50)     not null comment '教师名',
    age      int             null comment '年龄',
    email    varchar(50)     null comment '电子邮件',
    phone    varchar(50)     null comment '手机号码',
    sex      varchar(10) comment '性别',
    f_id     int comment '所在院系',
    t_id     int comment '职称',
    password varchar(50)     null comment '密码',
    foreign key fk_fid (f_id) references faculty (id) on delete cascade,
    foreign key fk_tid (t_id) references title (id)
);


# 课程表  衍生-->课程下学生管理
create table course
(
    id             int auto_increment not null primary key comment 'ID',
    cname          varchar(60)        not null comment '课程名',
    mark           varchar(50) comment '学分',
    t_id           int                not null comment '教师ID',
    room_id        int comment '教室号',
    max_choose_num int                not null comment '最大可选课人数',
    selected_num   int default 0 comment '已选人数',
    start_time     datetime comment '开始时间',
    end_time       datetime comment '结束时间',
    foreign key fk_tid (t_id) references teacher (id) on delete cascade,
    foreign key fk_rid (room_id) references room (id)
);


# 学生表
create table student
(
    id             int                             not null primary key auto_increment comment 'ID',
    sno            varchar(50) comment '学号' unique not null,
    name           varchar(50) comment '学生姓名'      not null,
    sex            varchar(10) comment '性别',
    password       varchar(20) comment '密码',
    age            int comment '年龄',
    f_id           int comment '所在院系',
    jiguan         varchar(60) comment '籍贯',
    mark           FLOAT(5, 2) comment '累计学分',
    email          varchar(50) comment '电子邮件',
    avatar         varchar(50) comment '头像',
    phone          varchar(50) comment '手机号码',
    max_choose_num int comment '最大可选课数',
    foreign key fk_fid (f_id) references faculty (id) on delete cascade

);

# 学生选课表
create table elective
(
    id    int auto_increment not null,
    st_id int comment '学生ID' not null,
    cs_id int comment '课程ID' not null,
    score float comment '成绩',
    foreign key fk_sid (st_id) references student (id) on delete cascade,
    foreign key fk_cid (cs_id) references course (id) on delete cascade,
    primary key (id, st_id, cs_id),
#  防止重复成绩
    unique (st_id, cs_id)
);

#---------------------------------------------------------------------------------------------------

# mock data时使用的触发器，正式上线需去除
delimiter ||
drop trigger if exists cs ||
create trigger cs
    after insert
    on elective
    for each row
begin
    update course set selected_num=selected_num + 1 where id = NEW.cs_id and selected_num < max_choose_num;
end ||;



# 触发器测试
delete from course where id>0;
delete from elective where id>0;
insert into elective(st_id, cs_id, score)
values (151,435,100) ;
