package cn.wchihc.jwc.servlets.admin;

import cn.wchihc.jwc.dao.base.BaseDao;
import cn.wchihc.jwc.model.other.Faculty;
import cn.wchihc.jwc.model.other.Room;
import cn.wchihc.jwc.model.other.Title;
import cn.wchihc.jwc.model.users.Admin;
import cn.wchihc.jwc.model.users.Password;
import cn.wchihc.jwc.model.users.Student;
import cn.wchihc.jwc.model.users.Teacher;
import cn.wchihc.jwc.servlets.admin.base.AdminServlet;
import cn.wchihc.jwc.utils.Constants;
import cn.wchihc.jwc.model.other.Course;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "管理员-改", urlPatterns = "/api/admin/update/*")
public class UpdateServlet extends AdminServlet {
    @Override
    protected void POST(String router, String jsonStr, HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.POST(router, jsonStr, request, response);
        switch (router) {
            case "faculty":
                handleUpdate(gson.fromJson(jsonStr, Faculty.class));
                break;
            case "title":
                handleUpdate(gson.fromJson(jsonStr, Title.class));
                break;
            case "classroom":
                handleUpdate(gson.fromJson(jsonStr, Room.class));
                break;
            case "course":
                handleUpdate(gson.fromJson(jsonStr, Course.class));
                break;
            case "teacher":
                handleUpdate(gson.fromJson(jsonStr, Teacher.class));
                break;
            case "student":
                handleUpdate(gson.fromJson(jsonStr, Student.class));
                break;
            case "admin":
                Object userId = request.getSession().getAttribute("user_id");
                if (userId != null) {
                    Admin admin1 = gson.fromJson(jsonStr, Admin.class);
                    if (admin1.id == Integer.parseInt((String) userId)) {
                        handleUpdate(admin1);
                    } else {
                        result.setCode(Constants.ILL_TOKEN_CODE);
                        result.setMessage("非法入侵操作,您在尝试篡改其他用户信息");
                    }
                } else {
                    result.setCode(Constants.ILL_TOKEN_CODE);
                    result.setMessage("您的登录信息存在异常");
                }

                break;
            case "adminPassword":

                Object userId1 = request.getSession().getAttribute("user_id");
                if (userId1 != null) {
                    Password passwords = gson.fromJson(jsonStr, Password.class);
                    if (passwords.getId() == Integer.parseInt((String) userId1)) {
                        if (BaseDao.select(Admin.class)
                                .where()
                                .append(BaseDao.eq("id", passwords.getId()))
                                .and()
                                .append(BaseDao.eq("password", passwords.getOldPassword().trim()))
                                .executeQuery().size() > 0
                        ) {
                            //验证成功,同意修改密码
                            Admin admin = new Admin();
                            admin.setId(passwords.getId());
                            admin.setPassword(passwords.getNewPassword().trim());

                            if (BaseDao.update(admin).executeUpdate()) {
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
                } else {
                    result.setCode(Constants.ILL_TOKEN_CODE);
                    result.setMessage("您的登录信息存在异常");
                }

                break;
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
