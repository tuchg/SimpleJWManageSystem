package cn.wchihc.jwc.servlets.admin;

import cn.wchihc.jwc.dao.base.BaseDao;
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

@WebServlet(name = "管理员-增", urlPatterns = "/api/admin/create/*")
public class CreateServlet extends AdminServlet {
    @Override
    protected void POST(String router, String jsonStr, HttpServletRequest request, HttpServletResponse response) {
        switch (router) {
            case "faculty":
                create(gson.fromJson(jsonStr, Faculty.class));
                break;
            case "title":
                create(gson.fromJson(jsonStr, Title.class));
                break;
            case "classroom":
                create(gson.fromJson(jsonStr, Room.class));
                break;
            case "course":
                create(gson.fromJson(jsonStr, Course.class));
                break;
            case "teacher":
                create(gson.fromJson(jsonStr, Teacher.class));
                break;
            case "student":
                create(gson.fromJson(jsonStr, Student.class));
                break;
        }
    }

    private void create(Object obj) {
        if (BaseDao.insert(obj).executeUpdate()) {
            result.setCode(Constants.SUCCESS_CODE);
            result.setMessage("创建成功");
        } else {
            result.setCode(Constants.FAILED_CODE);
            result.setMessage("创建失败,请重试");
        }
    }
}
