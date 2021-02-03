package cn.wchihc.jwc.servlets.common;

import cn.wchihc.jwc.utils.Constants;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;

@WebServlet(name = "通用-上传", urlPatterns = "/api/common/putImg")
@MultipartConfig
public class UploadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part part;
        try {
            part = request.getPart("file");
            String fileHeader = part.getHeader("content-disposition");
            String filename = part.getSubmittedFileName();
            String dir = "tmp/";

            String root = request.getRealPath(dir);

            File file = new File(root);
            if (!file.exists() || !file.isDirectory())
                file.mkdir();
            System.out.println(
                    part.getSize() + "\n" +
                            root + "\n" +
                            fileHeader + "\n" +
                            filename
            );
            part.write(root + filename);
            //偷懒 直接拼接json
            String test = "{\"code\":" + Constants.SUCCESS_CODE + ",\"message\":\"上传成功\",\"url\":\"" + dir + filename + "\"}";
            response.getWriter().println(test);
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
