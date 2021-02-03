package cn.wchihc.jwc.servlets.user;

import cn.wchihc.jwc.dao.base.BaseDao;
import cn.wchihc.jwc.model.base.Result;
import cn.w_chih_c.jwc.model.users.*;
import cn.wchihc.jwc.utils.Constants;
import cn.wchihc.jwc.utils.RequestBodyUtil;
import cn.wchihc.jwc.utils.TokenPool;
import cn.wchihc.jwc.model.users.*;
import cn.wzc.jwc.model.users.*;
import com.google.gson.Gson;

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
 * 用户接口
 */
@WebServlet(name = "用户-登录登出", urlPatterns = "/api/user/*")
public class LogInOutServlet extends HttpServlet {
    private Gson gson = new Gson();
    private Result<String> result = new Result<>();


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] splits = request.getRequestURI().split("/");

        switch (splits[splits.length - 1]) {
            case "login":
                login(request, response);
                break;
            case "logout":
                logout(request, response);
                break;
            default:
                // 避免路由污染
                return;
        }
        //        request.getSession().setAttribute("role", role);
//        gson.toJsonTree(result).getAsJsonObject().get()
        response.getWriter().println(gson.toJson(result));
    }

    /**
     * 用户登录
     * 1.判定用户组
     * 2.查询数据库
     * 3.生成token
     * 4.返回结果
     *
     * @param request
     * @param response
     */
    private void login(HttpServletRequest request, HttpServletResponse response) {
        String jsonStr = RequestBodyUtil.getContentBody(request);
        User user = gson.fromJson(jsonStr, User.class);
        if (user != null)
            switch (user.getRole()) {
                case "admin":
                    handleLogin(user, Admin.class);
                    break;
                case "student":
                    handleLogin(user, Student.class);
                    break;
                case "teacher":
                    handleLogin(user, Teacher.class);
                    break;
            }
        else {
            this.result.setCode(Constants.FAILED_CODE);
            this.result.setMessage("非法登录");
            this.result.setData(null);
        }
    }

    /**
     * 登录逻辑封装
     *
     * @param user
     * @param clazz
     */
    private <T> void handleLogin(User user, Class<T> clazz) {
        List<T> list = BaseDao.select(clazz)
                .where()
                .and(BaseDao.eq("name", user.getUsername()), BaseDao.eq("password", user.getPassword()))
                .executeQuery();
        if (list.size() > 0) {
            this.result.setCode(Constants.SUCCESS_CODE);
            this.result.setMessage("登录成功");
            // 根据角色及用户ID生成特色Token

            String token = TokenPool.genToken(
                    clazz.getSimpleName().toLowerCase(),
                    //通过父子关系解决 ID提取问题
                    ((Id) list.get(0)).id
            );
            this.result.setData(token);
        } else {
            this.result.setCode(Constants.FAILED_CODE);
            this.result.setMessage("账号或密码错误");
            this.result.setData(null);
        }
    }

    /**
     * 用户安全退出
     *
     * @param request
     * @param response
     */
    private void logout(HttpServletRequest request, HttpServletResponse response) {
        //清空session
        request.getSession().invalidate();
        // 从tokens里移除当前用户
        TokenPool.popToken(request.getHeader("X-Token").trim());

        this.result.setCode(Constants.SUCCESS_CODE);
        this.result.setMessage("退出成功");
        this.result.setData(null);
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }


}
