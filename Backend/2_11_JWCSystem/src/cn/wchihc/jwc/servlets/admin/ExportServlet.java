package cn.wchihc.jwc.servlets.admin;

import cn.wchihc.jwc.dao.base.BaseDao;
import cn.wchihc.jwc.model.Data;
import cn.wchihc.jwc.model.Query;
import cn.wchihc.jwc.model.other.Course;
import cn.wchihc.jwc.model.users.Student;
import cn.wchihc.jwc.model.users.Teacher;
import cn.wchihc.jwc.servlets.admin.base.AdminServlet;
import cn.wchihc.jwc.utils.Constants;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static cn.wchihc.jwc.dao.base.BaseDao.select;

@WebServlet(name = "管理员-导出", urlPatterns = "/api/admin/export/*")
public class ExportServlet extends AdminServlet {
    @Override
    protected void GET(String router, Query query, HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.GET(router, query, request, response);
        switch (router) {
            case "student":
                flatALL(Student.class);
                break;
            case "teacher":
                flatALL(Teacher.class);
                break;
            case "course":
                flatALL(Course.class);
                break;
        }

        result.setCode(Constants.SUCCESS_CODE);
        result.setMessage("查询成功");
    }

    private <T> void flatALL(Class<T> clazz) {
        List<T> tList = BaseDao.select(clazz).executeQuery();
        //数据封装
        Data<T> data = new Data<>();
        data.setTotal(tList.size());
        data.setItems(tList);
        //数据放入返回结果
        result.setData(data);
    }
}
