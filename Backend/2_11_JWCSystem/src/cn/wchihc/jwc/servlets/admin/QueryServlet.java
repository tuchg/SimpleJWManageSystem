package cn.wchihc.jwc.servlets.admin;

import cn.wchihc.jwc.dao.base.BaseDao;
import cn.wchihc.jwc.model.Data;
import cn.wchihc.jwc.model.Query;
import cn.wchihc.jwc.model.custom.ClassStudentCustom;
import cn.wchihc.jwc.model.custom.ClassroomCustom;
import cn.wchihc.jwc.model.custom.FacultyCustom;
import cn.wchihc.jwc.model.custom.TitleCustom;
import cn.wchihc.jwc.model.other.Faculty;
import cn.wchihc.jwc.model.other.Room;
import cn.wchihc.jwc.model.other.Title;
import cn.wchihc.jwc.model.users.Student;
import cn.wchihc.jwc.model.users.Teacher;
import cn.wchihc.jwc.servlets.admin.base.AdminServlet;
import cn.wchihc.jwc.utils.Constants;
import cn.wchihc.jwc.model.other.Course;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "管理员-查", urlPatterns = "/api/admin/list/*")
public class QueryServlet extends AdminServlet {

    @Override
    protected void GET(String router, Query query, HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.GET(router, query, request, response);
        switch (router) {
            case "faculty":
                //将自定义SQL送往计数处理并响应客户端
                handleDataQueryWrap(query, genFacultyQuery(query), FacultyCustom.class);
                break;
            case "title":
                handleDataQueryWrap(query, genTitleQuery(query), TitleCustom.class);
                break;
            case "classroom":
                handleDataQueryWrap(query, genClassRoomQuery(query), ClassroomCustom.class);
                break;
            case "course":
                handleDataQueryWrap(query, genCourseQuery(query), Course.class);
                break;
            case "teacher":
                handleDataQueryWrap(query, genTeacherQuery(query), Teacher.class);
                break;
            case "student":
                handleDataQueryWrap(query, genStudentQuery(query), Student.class);
                break;
            case "courseStudents":
                BaseDao<Student> dao = BaseDao.customSelect("student.id as s_id,name,sno,sex,e.id as e_id,f_id,cs_id as c_id", Student.class)
                        .join(" elective e", "student.id = e.st_id")
                        .where(BaseDao.eq("e.cs_id", query.getId()));
                handleDataQueryWrap(query, dao, ClassStudentCustom.class);
                break;
        }
        result.setCode(Constants.SUCCESS_CODE);
        result.setMessage("查询成功");
    }

    /**
     * 生成自定义复杂查询语句
     *
     * @param query
     * @return
     */
    private BaseDao<Student> genStudentQuery(Query query) {
        BaseDao<Student> students = BaseDao.select(Student.class)
                .where();
        //对查询的解析
        if (query.getFaculty() != null && !query.getFaculty().isEmpty()) {
            students.append(BaseDao.eq("f_id", query.getFaculty())).and();
        }
        students.like(query.getType().equals("1") ? "sno" : "name", query.getContent())
                .orderBy("sno", query.getSort());
        return students;
    }

    /**
     * 生成自定义复杂查询语句
     *
     * @param query
     * @return
     */
    private BaseDao<Teacher> genTeacherQuery(Query query) {
        BaseDao<Teacher> teachers = BaseDao.select(Teacher.class)
                .where();
        //对查询的解析
        if (query.getFaculty() != null && !query.getFaculty().isEmpty()) {
            teachers.append(BaseDao.eq("f_id", query.getFaculty())).and();
        }
        if (query.getTitle() != null && !query.getTitle().isEmpty()) {
            teachers.append(BaseDao.eq("t_id", query.getTitle())).and();
        }
        teachers.like("name", query.getContent())
                .orderBy("id", query.getSort());
        return teachers;
    }

    /**
     * 生成自定义复杂查询语句
     *
     * @param query
     * @return
     */
    private BaseDao<Course> genCourseQuery(Query query) {
        BaseDao<Course> c = BaseDao.select(Course.class).where();
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
        return c;
    }

    /**
     * 生成自定义复杂查询语句
     *
     * @param query
     * @return
     */
    private BaseDao<Room> genClassRoomQuery(Query query) {
        return BaseDao.customSelect(
                " r.*," +
                        "  count(c.room_id) as num_course ",
                Room.class,
                "r")
                .append("left")
                .join("course c", "r.id = c.room_id")
                .where().like("r.name", query.getContent())
                .groupBy("r.id")
                .orderBy("id", query.getSort());
    }

    /**
     * 生成自定义复杂查询语句
     *
     * @param query
     * @return
     */
    private BaseDao<Title> genTitleQuery(Query query) {
        return BaseDao.customSelect(
                " t.*," +
                        "  count(t2.t_id) as num_title ",
                Title.class,
                "t")
                .append("left")
                .join("teacher t2", "t.id= t2.t_id")
                .where().like("t.name", query.getContent())
                .groupBy("t.id")
                .orderBy("id", query.getSort());
    }

    /**
     * 生成自定义复杂查询语句
     *
     * @param query
     * @return
     */
    private BaseDao<Faculty> genFacultyQuery(Query query) {
        return BaseDao.customSelect(
                " f.*," +
                        " (select count(*) from student s where f.id = s.f_id) as num_student, " +
                        " (select count(*) from teacher t where f.id = t.f_id) as num_teacher",
                Faculty.class,
                "f"
        ).where().like("name", query.getContent()).orderBy("id", query.getSort());
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
        Data<C> titles = new Data<>(
                selects.getTotalSize(),
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
