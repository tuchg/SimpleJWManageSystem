package cn.wchihc.jwc.servlets.teacher;

import cn.wchihc.jwc.dao.base.BaseDao;
import cn.wchihc.jwc.model.other.Elective;
import cn.wchihc.jwc.model.users.Password;
import cn.wchihc.jwc.model.users.Teacher;
import cn.wchihc.jwc.servlets.teacher.base.TeacherServlet;
import cn.wchihc.jwc.utils.Constants;
import cn.wchihc.jwc.model.other.Course;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "教师-改", urlPatterns = "/api/teacher/update/*")
public class UpdateServlet extends TeacherServlet {
    @Override
    protected void POST(String router, String jsonStr, HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.POST(router, jsonStr, request, response);
        //从当前会话获取用户
        Object userId = request.getSession().getAttribute("user_id");
        if (userId != null) {
            switch (router) {
                case "course":
                    Course course = gson.fromJson(jsonStr, Course.class);
                    // todo 添加修改认证
                    handleUpdate(course);
                    break;
                case "myStudent":
                    Elective elective = gson.fromJson(jsonStr, Elective.class);
                    if (BaseDao.insert(elective).executeUpdate()) {
                        result.setCode(Constants.SUCCESS_CODE);
                        result.setMessage("录入成功");
                        return;
                    } else {
                        if (BaseDao.update(elective).executeUpdate()) {
                            result.setCode(Constants.SUCCESS_CODE);
                            result.setMessage("修改成功");
                            return;
                        }
                    }
                    result.setCode(Constants.FAILED_CODE);
                    result.setMessage("录入失败");
                    break;
                case "teacher":
                    Teacher teacher = gson.fromJson(jsonStr, Teacher.class);
                    if (teacher.id.equals(userId)) {
                        handleUpdate(teacher);
                    } else {
                        result.setCode(Constants.ILL_TOKEN_CODE);
                        result.setMessage("非法入侵操作,您在尝试篡改其他用户信息");
                    }
                    break;
                case "teacherPassword":
                    Password passwords = gson.fromJson(jsonStr, Password.class);
                    if (passwords.getId() == (Integer) userId) {
                        if (BaseDao.select(Teacher.class)
                                .where()
                                .append(BaseDao.eq("id", passwords.getId()))
                                .and()
                                .append(BaseDao.eq("password", passwords.getOldPassword().trim()))
                                .executeQuery().size() > 0
                        ) {
                            //验证成功,同意修改密码
                            Teacher t = new Teacher();
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
