package cn.wchihc.jwc.servlets.student;

import cn.wchihc.jwc.dao.base.BaseDao;
import cn.wchihc.jwc.servlets.student.base.StudentServlet;
import cn.wchihc.jwc.utils.Constants;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "学生-增", urlPatterns = "/api/student/create/*")
public class CreateServlet extends StudentServlet {
    @Override
    protected void POST(String router, String jsonStr, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //从当前会话获取用户
        Object userId = request.getSession().getAttribute("user_id");
        if ("elective".equals(router)) {
            ArrayList<Double> list = gson.fromJson(jsonStr, ArrayList.class);
            //将double解析为int 去除小数点
            List<Integer> integers = list.stream()
                    .mapToInt(Double::intValue)
                    .boxed()
                    .collect(Collectors.toList());
            if (userId != null) {
                BaseDao dao =
                        BaseDao.custom("insert into elective (st_id, cs_id) ")
                                .append("values");
                //需要插入的每一项
                integers.forEach(i -> {
                    dao.values(userId, i).append(",");
                });
                dao.delRear();

                if (dao.executeUpdate())
                    //去将选课情况同步到课程
                    if (BaseDao.custom("update course set selected_num = (selected_num+1)")
                            .where("course.id")
                            .in(integers.toArray())
                            .executeUpdate()) {
                        result.setCode(Constants.SUCCESS_CODE);
                        result.setMessage("选课成功");
                        return;
                    }
                result.setCode(Constants.FAILED_CODE);
                result.setMessage("选课失败,请重试");
            } else {
                result.setCode(Constants.ILL_TOKEN_CODE);
                result.setMessage("登录信息丢失");
            }
        }
    }
}
