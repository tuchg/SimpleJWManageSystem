package cn.wchihc.jwc.servlets.student;

import cn.wchihc.jwc.dao.base.BaseDao;
import cn.wchihc.jwc.model.Data;
import cn.wchihc.jwc.model.Query;
import cn.wchihc.jwc.model.custom.ClassStudentCustom;
import cn.wchihc.jwc.model.custom.ScoreReportCustom;
import cn.wchihc.jwc.model.other.Elective;
import cn.wchihc.jwc.model.users.Teacher;
import cn.wchihc.jwc.servlets.student.base.StudentServlet;
import cn.wchihc.jwc.utils.Constants;
import cn.wchihc.jwc.model.other.Course;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static cn.wchihc.jwc.dao.base.BaseDao.*;

@WebServlet(name = "学生-查", urlPatterns = "/api/student/list/*")
public class QueryServlet extends StudentServlet {

    @Override
    protected void GET(String router, Query query, HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.GET(router, query, request, response);
        //从当前会话获取用户
        Object userId = request.getSession().getAttribute("user_id");
        if (userId != null) {
            switch (router) {
                case "course":
                    handleQuery(query, "!", userId);
                    break;
                case "myCourse":
                    handleQuery(query, "", userId);
                    break;
                case "myScore":
                    handleScoreQuery(query, (Integer) userId);
                    break;
                //班级学生排名
                case "classRank":
                    handleDataQueryWrap(query, getElectiveBaseDao(query), ClassStudentCustom.class);
                    break;
            }
        } else {
            result.setCode(Constants.ILL_TOKEN_CODE);
            result.setMessage("登录信息丢失");
        }

    }

    /**
     * 处理成绩单查询
     *
     * @param query
     * @param userId
     */
    private void handleScoreQuery(Query query, Integer userId) {

        BaseDao<Course> baseDao = BaseDao.customSelect("course.*, score", Course.class)
                .append("left")
                .join("elective", "course.id = elective.cs_id")
                .where(BaseDao.eq("st_id", userId)).and();

        switch (query.getStatus()) {
            case "优秀":
                baseDao.append("score>80").and();
                break;
            case "良好":
                baseDao.append(" score between  70 and 80 ").and();
                break;
            case "及格":
                baseDao.append("score between  60 and 70").and();
                break;
            case "不及格":
                baseDao.append("score <60").and();
                break;
        }
        //未查找到指定老师退出
        if (!contentSearch(query, baseDao)) return;

        int size = baseDao.getTotalSize();
        baseDao.orderBy("course.id", query.getSort());
        //分页处理
        List<ScoreReportCustom> byPage = findByPage(query, baseDao, ScoreReportCustom.class);

        //效率极低 轮询数据库已选各课程的班级成绩单 获取成绩班级排名
        byPage.forEach(i -> {
            List<ClassStudentCustom> students = BaseDao.customSelect("rank() over (order by score desc ) as s_rank, name,sex,f_id,score,st_id as s_id", Elective.class)
                    .append("left").join("student s", "elective.st_id = s.id")
                    .where(eq("cs_id", i.getId())).executeCustomQuery(ClassStudentCustom.class);
            i.setSNum(students.size());
            for (ClassStudentCustom e : students) {
                if (e.getSId().equals(userId)) {
                    i.setSRank(e.getSRank());
                    return;
                }
            }
        });

        result.setCode(Constants.SUCCESS_CODE);
        result.setMessage("查询成功");
        result.setData(new Data<>(size, byPage));
    }

    /**
     * 班级学生排名信息
     *
     * @param query
     * @return
     */
    private BaseDao<Elective> getElectiveBaseDao(Query query) {
        return BaseDao.customSelect("rank() over (order by score desc ) as s_rank, s.id as s_id,sno,name,sex,f_id,score", Elective.class)
                .append("left").join("student s", "elective.st_id = s.id")
                .where(BaseDao.eq("cs_id", query.getId()));
    }

    /**
     * @param query
     * @param isEq   !查询未选 查询已选
     * @param userId
     */
    private void handleQuery(Query query, String isEq, Object userId) {
        BaseDao<Course> c = BaseDao.customSelect("course.*", Course.class)
                .append(" left ").join(" elective", "course.id = elective.cs_id")
                //选中当前学生未选择的课   ::: is  null为了将无人选择的课并入结果集
                .where("elective.st_id " + isEq + "= " + userId)
                .and();

        if (!isEq.isEmpty()) {
            c.append(BaseDao.eq("elective.cs_id not in (select cs_id from elective where st_id", userId.toString())).append(")")
                    .append(" or elective.st_id is null")
                    .and();
        }
        switch (query.getStatus()) {
            case "已满":
                c.append("max_choose_num=selected_num").and();
                break;
            case "未满":
                c.append("not max_choose_num=selected_num").and();
                break;
        }
        if (!contentSearch(query, c)) return;
        c.groupBy("course.id");
        c.orderBy("course.id", query.getSort());
        handleDataQueryWrap(query, c, Course.class);
        result.setCode(Constants.SUCCESS_CODE);
        result.setMessage("查询成功");
    }

    /**
     * 查询老师
     *
     * @param query
     * @param c
     * @return false 未查找到老师
     */
    private boolean contentSearch(Query query, BaseDao<Course> c) {
        //默认搜索课程名
        String type = "cname";
        //判断是否查询教师名
        if (query.getType().equals("1")) {
            List<Teacher> teachers = BaseDao.select(Teacher.class)
                    .where()
                    .like("name", query.getContent())
                    .executeQuery();
            if (teachers.size() > 0) {
                //将查询到的教师id交给原课程查询
                c.append(BaseDao.eq("t_id", teachers.get(0).getId()));
            } else {
                //无结果
                result.setCode(Constants.SUCCESS_CODE);
                result.setData(null);
                return false;
            }
        } else
            c.like(type, query.getContent());
        return true;
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
