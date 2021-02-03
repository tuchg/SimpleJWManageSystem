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

import static cn.wchihc.jwc.dao.base.BaseDao.eq;

@WebServlet(name = "学生-删", urlPatterns = "/api/student/del/*")
public class DeleteServlet extends StudentServlet {
    @Override
    protected void POST(String router, String jsonStr, HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.POST(router, jsonStr, request, response);
        //从当前会话获取用户
        Object userId = request.getSession().getAttribute("user_id");
        if ("elective".equals(router)) {
            //可能是因为不确定类型故而转换为最大的基本类型
            ArrayList<Double> list = gson.fromJson(jsonStr, ArrayList.class);
            //将double解析为int 去除小数点
            List<Integer> integers = list.stream()
                    .mapToInt(Double::intValue)
                    .boxed()
                    .collect(Collectors.toList());

            if (userId != null) {
                BaseDao dao =
                        BaseDao.custom("delete from elective ")
                                .where("cs_id")
                                .in(integers.toArray())
                                .and().append(BaseDao.eq("st_id", (Integer) userId));
                if (dao.executeUpdate())
                    //去将选课情况同步到课程
                    if (BaseDao.custom("update course set selected_num = (selected_num-1)")
                            .where("course.id")
                            .in(integers.toArray())
                            .executeUpdate()) {
                        result.setCode(Constants.SUCCESS_CODE);
                        result.setMessage("退课成功");
                        return;
                    }

                result.setCode(Constants.FAILED_CODE);
                result.setMessage("退课失败,请重试");

            } else {
                result.setCode(Constants.ILL_TOKEN_CODE);
                result.setMessage("登录信息丢失");
            }
        }
    }
}
