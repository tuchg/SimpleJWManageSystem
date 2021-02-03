package cn.wchihc.jwc.servlets.admin;

import cn.wchihc.jwc.dao.base.BaseDao;
import cn.wchihc.jwc.model.other.Faculty;
import cn.wchihc.jwc.model.other.Room;
import cn.wchihc.jwc.model.other.Title;
import cn.wchihc.jwc.model.users.Id;
import cn.wchihc.jwc.model.users.Student;
import cn.wchihc.jwc.model.users.Teacher;
import cn.wchihc.jwc.servlets.admin.base.AdminServlet;
import cn.wchihc.jwc.utils.Constants;
import cn.wchihc.jwc.model.other.Course;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "管理员-删", urlPatterns = "/api/admin/del/*")
public class DeleteServlet extends AdminServlet {
    @Override
    protected void POST(String router, String jsonStr, HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.POST(router, jsonStr, request, response);
        Id id = gson.fromJson(jsonStr, Id.class);
        switch (router) {
            case "faculty":
                Faculty faculty = new Faculty();
                faculty.setId(id.id);
                handleDel(faculty);
                break;
            case "title":
                Title title = new Title();
                title.setId(id.id);
                handleDel(title);
                break;
            case "classroom":
                Room room = new Room();
                room.setId(id.id);
                handleDel(room);
                break;
            case "course":
                Course course = new Course();
                course.setId(id.id);
                handleDel(course);
                break;
            case "teacher":
                Teacher teacher = new Teacher();
                teacher.setId(id.id);
                handleDel(teacher);
                break;
            case "student":
                Student student = new Student();
                student.setId(id.id);
                handleDel(student);
                break;
            case "courseStudent":
                BaseDao dao =
                        BaseDao.custom("delete from elective ")
                                .where(BaseDao.eq("cs_id", id.id))
                                .and().append(BaseDao.eq("st_id", id.id2));
                if (dao.executeUpdate())
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
