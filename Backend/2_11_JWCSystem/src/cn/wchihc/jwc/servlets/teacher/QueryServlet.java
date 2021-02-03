package cn.wchihc.jwc.servlets.teacher;

import cn.wchihc.jwc.dao.base.BaseDao;
import cn.wchihc.jwc.model.Data;
import cn.wchihc.jwc.model.Query;
import cn.wchihc.jwc.model.custom.ClassStudentCustom;
import cn.wchihc.jwc.model.custom.MyStudentCustom;
import cn.wchihc.jwc.model.users.Student;
import cn.wchihc.jwc.servlets.teacher.base.TeacherServlet;
import cn.wchihc.jwc.utils.Constants;
import cn.wchihc.jwc.model.other.Course;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "教师-查", urlPatterns = "/api/teacher/list/*")
public class QueryServlet extends TeacherServlet {

    @Override
    protected void GET(String router, Query query, HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.GET(router, query, request, response);
        //从当前会话获取用户
        Object userId = request.getSession().getAttribute("user_id");
        if (userId != null) {
            switch (router) {
                case "course":

                    BaseDao<Course> c = BaseDao.select(Course.class)
                            .where(BaseDao.eq("t_id", (Integer) userId))
                            .and();
                    switch (query.getStatus()) {
                        case "已满":
                            c.append("max_choose_num=selected_num").and();
                            break;
                        case "未满":
                            c.append("not max_choose_num=selected_num").and();
                            break;
                    }
                    c.like("cname", query.getContent())
                            .orderBy("id", query.getSort());

                    handleDataQueryWrap(query, c, Course.class);

                    result.setCode(Constants.SUCCESS_CODE);
                    result.setMessage("查询成功");

                    break;
                case "student":

                    BaseDao<Course> courseBaseDao =
                            BaseDao.customSelect("elective.id,elective.cs_id,elective.st_id,s.sno,s.name,course.cname,s.sex,s.f_id,elective.score", Course.class)
                                    .join("elective", "course.id = elective.cs_id")
                                    .join("student s", "elective.st_id = s.id")
                                    .where(BaseDao.eq("t_id", (Integer) userId))
                                    .and();
                    //对查询的解析
                    if (query.getFaculty() != null && !query.getFaculty().isEmpty()) {
                        courseBaseDao.append(BaseDao.eq("f_id", query.getFaculty()))
                                .and();
                    }
                    //默认搜索姓名
                    String type = "name";
                    switch (query.getType()) {
                        case "1":
                            type = "sno";
                            break;
                        case "2":
                            type = "cname";
                            break;
                    }

                    courseBaseDao.like(type, query.getContent())
                            .orderBy("sno", query.getSort());
                    handleDataQueryWrap(query, courseBaseDao, MyStudentCustom.class);

                    result.setCode(Constants.SUCCESS_CODE);
                    result.setMessage("查询成功");
                    break;
                case "courseStudents":
                    BaseDao<Student> dao = BaseDao.customSelect("student.id as s_id,name,sno,sex,e.id as e_id,f_id,cs_id as c_id", Student.class)
                            .join(" elective e", "student.id = e.st_id")
                            .where(BaseDao.eq("e.cs_id", query.getId()));
                    handleDataQueryWrap(query, dao, ClassStudentCustom.class);
                    break;
            }

        } else {
            result.setCode(Constants.ILL_TOKEN_CODE);
            result.setMessage("登录信息丢失");
        }

    }


    /**
     * 分页处理复合查询
     * <p>
     * 并打包要用来响应的数据
     *
     * @param query
     * @param selects
     * @param clazz
     * @param <T>
     * @param <C>
     */
    private <T, C> void handleDataQueryWrap(Query query, BaseDao<T> selects, Class<C> clazz) {
        Data<C> titles = new Data<>(selects.getTotalSize(),
                findByPage(query, selects, clazz));
        result.setData(titles);
    }


    /**
     * 分页
     *
     * @param query   分页查询串
     * @param selects 自定义查询语句
     * @param clazz   返回类型
     * @param <T>     查询的主表
     * @param <C>     返回的类型
     * @return 分页查询结果
     */
    private <T, C> List<C> findByPage(Query query, BaseDao<T> selects, Class<C> clazz) {
        return selects.byPage(query.getPage(), query.getLimit())
                .executeCustomQuery(clazz);
    }
}
