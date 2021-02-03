# SimpleJWAdminSystem
一个基于Servlet3 + JSON + vue-element-admin + mysql8 的前后端分离的教务管理系统（JSP课程设计）

### 实现的特色功能
* Java SQL 
    ```sql
    # 常规查询
    select * from user; 
    
    # 复杂查询
    select  r.*,  count(c.room_id) as num_course 
    from room r 
        left join course c on r.id = c.room_id
     where  r.name like '%搜索%' 
     group by r.id  
     order by id asc  
     limit 0,20; 
   ```

   ```java
   // 常规查询
   select(User.class).executeQuery(); //-> List<User>
  // 复杂查询
   customSelect(" r.*,count(c.room_id) as num_course ",
                Room.class,"r")
                .append("left")
                .join("course c", "r.id = c.room_id")
                .where().like("r.name", "搜索")
                .groupBy("r.id")
                .orderBy("id", "1")
                .limit(0,20)
                .executeCustomQuery();
   ```
* 基于URL * 匹配机制下的 ~~"RESTful"~~ 路由API
* 继承机制下的多用户权限管理
* 基于JSON的经典前后端分离设计
* 支持批量导入导出Excel的Vue组件封装
  
> 如果觉得有所帮助,就请给我一个Star吧,谢谢

### 系统功能模块图
![](/Screenshots/图片1.png)

### 前端git日志
![](/Screenshots/git.png)
### 预览
![登录页面](/Screenshots/登录失败.png)
![管理员首页](/Screenshots/管理员首页.png)
![教师管理](/Screenshots/教师管理.png)
![导出](/Screenshots/导出.png)
![打印](/Screenshots/成绩单打印.png)
![选课](/Screenshots/选课.png)
![创建](/Screenshots/课程创建.png)
