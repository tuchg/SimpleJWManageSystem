package cn.wchihc.jwc.servlets.common;

import cn.wchihc.jwc.dao.base.BaseDao;
import cn.wchihc.jwc.model.base.Result;
import cn.wchihc.jwc.model.other.Faculty;
import cn.wchihc.jwc.model.other.Room;
import cn.wchihc.jwc.model.other.Title;
import cn.wchihc.jwc.model.users.Teacher;
import cn.wchihc.jwc.utils.Constants;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static cn.wchihc.jwc.dao.base.BaseDao.select;

@WebServlet(name = "通用接口", urlPatterns = "/api/common/*")
public class ListServlet extends HttpServlet {
    private Gson gson = new Gson();
    private Result result = new Result<>();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] splits = request.getRequestURI().split("/");
        switch (splits[splits.length - 1]) {
            case "facultyList":
                findData(Faculty.class);
                break;
            case "classRoomList":
                findData(Room.class);
                break;
            case "titleList":
                findData(Title.class);
                break;
            case "teacherList":
                findData(Teacher.class);
                break;

            default:
                // 避免路由污染
                return;
        }
        response.getWriter().println(gson.toJson(result));
    }

    private <T> void findData(Class<T> clazz) {
        List<T> list = BaseDao.select(clazz).executeQuery();
        result.setCode(Constants.SUCCESS_CODE);
        result.setMessage("查询成功");
        result.setData(list);
    }
}
