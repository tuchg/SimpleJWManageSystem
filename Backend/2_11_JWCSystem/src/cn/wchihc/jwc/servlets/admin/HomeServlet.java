package cn.wchihc.jwc.servlets.admin;

import cn.wchihc.jwc.dao.base.BaseDao;
import cn.wchihc.jwc.model.Query;
import cn.wchihc.jwc.model.other.Faculty;
import cn.wchihc.jwc.model.users.Student;
import cn.wchihc.jwc.model.users.Teacher;
import cn.wchihc.jwc.servlets.admin.base.AdminServlet;
import cn.wchihc.jwc.utils.Constants;
import cn.wchihc.jwc.model.custom.HomeCustom;
import cn.wchihc.jwc.model.other.SimpleChartData;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static cn.wchihc.jwc.dao.base.BaseDao.customSelect;

@WebServlet(name = "管理员-面板", urlPatterns = "/api/admin/home")
public class HomeServlet extends AdminServlet {
    @Override
    protected void GET(String router, Query query, HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.GET(router, query, request, response);
        int studentNum = BaseDao.executeCount(Student.class);
        int teacherNum = BaseDao.executeCount(Teacher.class);
        int facultyNum = BaseDao.executeCount(Faculty.class);

        List<SimpleChartData> data = BaseDao.customSelect("faculty.name, count(*) as value", Faculty.class)
                .append("left").join("student s", "faculty.id = s.f_id")
                .groupBy("faculty.id")
                .executeCustomQuery(SimpleChartData.class);

        result.setCode(Constants.SUCCESS_CODE);
        result.setData(new HomeCustom<>(studentNum, teacherNum, facultyNum, data));
    }
}
