package cn.wchihc.jwc.servlets.teacher;

import cn.wchihc.jwc.dao.base.BaseDao;
import cn.wchihc.jwc.model.users.Id;
import cn.wchihc.jwc.servlets.teacher.base.TeacherServlet;
import cn.wchihc.jwc.utils.Constants;
import cn.wchihc.jwc.model.other.Course;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "教师-删", urlPatterns = "/api/teacher/del/*")
public class DeleteServlet extends TeacherServlet {
    @Override
    protected void POST(String router, String jsonStr, HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.POST(router, jsonStr, request, response);
        Id id = gson.fromJson(jsonStr, Id.class);
        switch (router) {
            case "course":
                Course course = new Course();
                course.setId(id.id);
                handleDel(course);
                break;
            case "courseStudent":
                if (BaseDao.custom("delete from elective ")
                        .where(BaseDao.eq("cs_id", id.id))
                        .and().append(BaseDao.eq("st_id", id.id2))
                        .executeUpdate())
                    //去将选课情况同步到课程
                    if (BaseDao.custom("update course set selected_num = (selected_num-1)")
                            .where(BaseDao.eq("course.id", id.id))
                            .executeUpdate()) {
                        result.setCode(Constants.SUCCESS_CODE);
                        result.setMessage("劝退成功");
                        return;
                    }
                result.setCode(Constants.FAILED_CODE);
                result.setMessage("退课失败,请重试");
                break;
        }
    }

    private void handleDel(Object object) {
        if (BaseDao.del(object).executeUpdate()) {
            result.setCode(Constants.SUCCESS_CODE);
            result.setMessage("删除成功");
        } else {
            result.setCode(Constants.FAILED_CODE);
            result.setMessage("删除失败");
        }
    }
}
