package cn.wchihc.jwc.servlets.admin;

import cn.wchihc.jwc.dao.base.BaseDao;
import cn.wchihc.jwc.model.other.Course;
import cn.wchihc.jwc.model.users.Student;
import cn.wchihc.jwc.model.users.Teacher;
import cn.wchihc.jwc.servlets.admin.base.AdminServlet;
import cn.wchihc.jwc.utils.Constants;
import com.google.gson.reflect.TypeToken;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "管理员-导入", urlPatterns = "/api/admin/import/*")
public class ImportServlet extends AdminServlet {

    private String jsonStr;

    @Override
    protected void POST(String router, String jsonStr, HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.POST(router, jsonStr, request, response);
        this.jsonStr = jsonStr;
        switch (router) {
            case "student":
                insertBatch(Student.class, new TypeToken<List<Student>>() {
                });
                break;
            case "teacher":
                insertBatch(Teacher.class, new TypeToken<List<Teacher>>() {
                });
                break;
            case "course":
                insertBatch(Course.class, new TypeToken<List<Course>>() {
                });
                break;
        }
    }

    private void insertBatch(Class clazz, TypeToken typeToken) {
        if (BaseDao.batchInsert(
                clazz,
                gson.fromJson(jsonStr, typeToken.getType())
        ).executeUpdate()) {
            result.setCode(Constants.SUCCESS_CODE);
            result.setMessage("导入成功");
        } else {
            result.setCode(Constants.FAILED_CODE);
            result.setMessage("导入失败,请重试");
        }
    }
}
