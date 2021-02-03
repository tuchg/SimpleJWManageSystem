package cn.wchihc.jwc.servlets.user;

import cn.wchihc.jwc.dao.base.BaseDao;
import cn.wchihc.jwc.model.base.Result;
import cn.wchihc.jwc.utils.Constants;
import cn.wchihc.jwc.model.users.Admin;
import cn.wchihc.jwc.model.users.Student;
import cn.wchihc.jwc.model.users.Teacher;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static cn.wchihc.jwc.dao.base.BaseDao.eq;
import static cn.wchihc.jwc.dao.base.BaseDao.select;

/**
 * 用户信息接口
 */
@WebServlet(name = "用户-获取信息", urlPatterns = "/api/user/info")
public class UserInfoServlet extends HttpServlet {
    private Gson gson = new Gson();
    private Result result = new Result<>();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Object userId = request.getSession().getAttribute("user_id");
        Object role = request.getSession().getAttribute("role");

        if (role != null && userId != null) {
            switch ((String) role) {
                case "admin":
                    handleGET((int) userId, Admin.class);
                    break;
                case "student":
                    handleGET((int) userId, Student.class);
                    break;
                case "teacher":
                    handleGET((int) userId, Teacher.class);
                    break;
            }
        }
        // 修改JSON添加用户组后返回JSON数据
        JsonElement jsonElement = gson.toJsonTree(result);

        JsonArray roles = new JsonArray();
        roles.add((String) role);

        JsonObject object = jsonElement
                .getAsJsonObject()
                .get("data")
                .getAsJsonObject();
        //从返回消息去除密码段
        object.remove("password");
        //填充用户组
        object.getAsJsonObject()
                .add("roles", roles);
        response.getWriter().println(jsonElement);
    }

    /**
     * 从数据库获取各用户信息
     *
     * @param id
     * @param clazz
     * @param <T>
     */
    private <T> void handleGET(int id, Class<T> clazz) {
        List<T> list = BaseDao.select(clazz)
                .where(BaseDao.eq("id", id))
                .executeQuery();

        if (list.size() > 0) {
            this.result.setCode(Constants.SUCCESS_CODE);
            this.result.setMessage("登录成功");
            this.result.setData(list.get(0));
        } else {
            this.result.setCode(Constants.ILL_TOKEN_CODE);
            this.result.setMessage("非法入侵操作!!");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    }

}
