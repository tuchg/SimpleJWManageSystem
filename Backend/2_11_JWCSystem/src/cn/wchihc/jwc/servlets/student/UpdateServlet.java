package cn.wchihc.jwc.servlets.student;

import cn.wchihc.jwc.dao.base.BaseDao;
import cn.wchihc.jwc.model.users.Password;
import cn.wchihc.jwc.model.users.Student;
import cn.wchihc.jwc.servlets.student.base.StudentServlet;
import cn.wchihc.jwc.utils.Constants;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "学生-改", urlPatterns = "/api/student/update/*")
public class UpdateServlet extends StudentServlet {
    @Override
    protected void POST(String router, String jsonStr, HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.POST(router, jsonStr, request, response);
        //从当前会话获取用户
        Object userId = request.getSession().getAttribute("user_id");
        if (userId != null) {
            switch (router) {
                case "student":
                    Student student = gson.fromJson(jsonStr, Student.class);
                    if (student.id.equals(userId)) {
                        handleUpdate(student);
                    } else {
                        result.setCode(Constants.ILL_TOKEN_CODE);
                        result.setMessage("非法入侵操作,您在尝试篡改其他用户信息");
                    }
                    break;
                case "studentPassword":
                    Password passwords = gson.fromJson(jsonStr, Password.class);
                    if (passwords.getId() == (Integer) userId) {
                        if (BaseDao.select(Student.class)
                                .where()
                                .append(BaseDao.eq("id", passwords.getId()))
                                .and()
                                .append(BaseDao.eq("password", passwords.getOldPassword().trim()))
                                .executeQuery().size() > 0
                        ) {
                            //验证成功,同意修改密码
                            Student t = new Student();
                            t.setId(passwords.getId());
                            t.setPassword(passwords.getNewPassword().trim());

                            if (BaseDao.update(t).executeUpdate()) {
                                result.setCode(Constants.SUCCESS_CODE);
                                result.setMessage("密码修改成功");
                            } else {
                                result.setCode(Constants.FAILED_CODE);
                                result.setMessage("修改失败");
                            }
                        } else {
                            result.setCode(Constants.FAILED_CODE);
                            result.setMessage("旧密码输入错误");
                        }
                    } else {
                        result.setCode(Constants.ILL_TOKEN_CODE);
                        result.setMessage("非法入侵操作,您在尝试篡改其他用户信息");
                    }
                    break;
            }
        } else {
            result.setCode(Constants.ILL_TOKEN_CODE);
            result.setMessage("登录信息丢失");
        }
    }

    private void handleUpdate(Object o) {
        if (BaseDao.update(o).executeUpdate()) {
            result.setCode(Constants.SUCCESS_CODE);
            result.setMessage("更新成功");
        } else {
            result.setCode(Constants.FAILED_CODE);
            result.setMessage("更新失败");
        }
    }
}
