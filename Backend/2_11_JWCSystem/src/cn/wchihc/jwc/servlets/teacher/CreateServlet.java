package cn.wchihc.jwc.servlets.teacher;

import cn.wchihc.jwc.dao.base.BaseDao;
import cn.wchihc.jwc.servlets.teacher.base.TeacherServlet;
import cn.wchihc.jwc.utils.Constants;
import cn.wchihc.jwc.model.other.Course;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "教师-增", urlPatterns = "/api/teacher/create/*")
public class CreateServlet extends TeacherServlet {
    @Override
    protected void POST(String router, String jsonStr, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //从当前会话获取用户
        Object userId = request.getSession().getAttribute("user_id");
        if ("course".equals(router)) {
            Course course = gson.fromJson(jsonStr, Course.class);

            if (userId != null) {

                course.setTId((Integer) userId);
                create(course);

            } else {
                result.setCode(Constants.ILL_TOKEN_CODE);
                result.setMessage("登录信息丢失");
            }
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
