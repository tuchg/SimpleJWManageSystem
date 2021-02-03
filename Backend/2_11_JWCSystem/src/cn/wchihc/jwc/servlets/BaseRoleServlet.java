package cn.wchihc.jwc.servlets;

import cn.wchihc.jwc.model.Query;
import cn.wchihc.jwc.model.base.Result;
import cn.wchihc.jwc.utils.Constants;
import cn.wchihc.jwc.utils.RequestBodyUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用于识别当前请求的所属用户组
 * 判断被放行的token是否是当前操作API的权限范围
 */
public class BaseRoleServlet extends HttpServlet {

    protected Gson gson;
    protected Result<Object> result = new Result<>();
    private String role;

    /**
     * 初始化时需指定当前角色
     *
     * @param role
     */
    public BaseRoleServlet(String role) {
        super();
        this.role = role;
        gson = new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(Integer.class, new TypeAdapter<Number>() {
                    @Override
                    public void write(JsonWriter out, Number value)
                            throws IOException {
                        out.value(value);
                    }

                    @Override
                    public Number read(JsonReader in) throws IOException {
                        if (in.peek() == JsonToken.NULL) {
                            in.nextNull();
                            return null;
                        }
                        try {
                            String result = in.nextString();
                            if ("".equals(result)) {
                                return null;
                            }
                            return Integer.parseInt(result);
                        } catch (NumberFormatException e) {
                            throw new JsonSyntaxException(e);
                        }
                    }
                })
                .registerTypeAdapter(Timestamp.class, new TypeAdapter<Timestamp>() {
                    private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");

                    @Override
                    public void write(JsonWriter out, Timestamp value) throws IOException {
                        out.value(value == null ? null : format.format(value));
                    }

                    @Override
                    public Timestamp read(JsonReader in) throws IOException {
                        if (in.peek() == JsonToken.NULL) {
                            in.nextNull();
                            return null;
                        }
                        try {
                            final long utilDate = format.parse(in.nextString()).getTime();
                            return new Timestamp(utilDate);
                        } catch (ParseException e) {
                            throw new JsonSyntaxException(e);
                        }
                    }

                })
                .registerTypeAdapter(java.sql.Date.class, new TypeAdapter<java.sql.Date>() {
                    private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");

                    @Override
                    public void write(JsonWriter out, Date value) throws IOException {
                        out.value(value == null ? null : format.format(value));
                    }

                    @Override
                    public Date read(JsonReader in) throws IOException {
                        if (in.peek() == JsonToken.NULL) {
                            in.nextNull();
                            return null;
                        }
                        try {
                            final long utilDate = format.parse(in.nextString()).getTime();
                            return new java.sql.Date(utilDate);
                        } catch (ParseException e) {
                            throw new JsonSyntaxException(e);
                        }
                    }

                })
                .create();
    }

    /**
     * POST
     *
     * @param router   当前路由
     * @param jsonStr  JSON
     * @param request  请求
     * @param response 响应
     */
    protected void POST(String router, String jsonStr, HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    /**
     * GET
     *
     * @param router   当前路由
     * @param request  请求
     * @param response 响应
     */
    protected void GET(String router, Query query, HttpServletRequest request, HttpServletResponse response) throws IOException {
    }

    /**
     * 接口权限检测
     *
     * @param request  request
     * @param response response
     * @throws IOException
     */
    private boolean checkCurrentRole(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Object role = request.getSession().getAttribute("role");
        return this.role.equals(role);
    }

    /**
     * 切分urls 制作路由
     *
     * @param request
     * @return
     */
    private String getUrl(HttpServletRequest request) {
        String[] split = request.getRequestURI().split("/");
        return split[split.length - 1];
    }

    /**
     * 权限检测处理
     *
     * @param method
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    private void roleCheckHandle(String method, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (checkCurrentRole(request, response)) {

                switch (method) {
                    case "get":
                        //将请求参数 url 转变为json
                        Map<String, String> map = request.getParameterMap()
                                .entrySet()
                                .stream()
                                .collect(Collectors.toMap(
                                        Map.Entry::getKey,
                                        value -> value.getValue()[0]
                                ));
                        GET(getUrl(request), gson.fromJson(gson.toJson(map), Query.class), request, response);
                        break;
                    case "post":
                        String jsonStr = RequestBodyUtil.getContentBody(request);
                        if (!jsonStr.isEmpty()) {
                            POST(getUrl(request), jsonStr, request, response);
                        } else {
                            result.setCode(Constants.FAILED_CODE);
                            result.setMessage("非法访问");
                            //                        response.setStatus(401);
                            //                        response.sendError(401);
                        }
                        break;
                }
            } else {
                // 非法访问
                result.setCode(Constants.ILL_TOKEN_CODE);
                result.setMessage("非法访问");
                //            response.setStatus(401);
                //            response.sendError(401);
                /* request.getRequestDispatcher(request.getContextPath() + "/#/401").forward(request, response);*/
                //发送响应结果
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(Constants.FAILED_CODE);
            result.setMessage("服务器发生异常");
        } finally {
            try {
                response.getWriter().println(gson.toJson(result));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        roleCheckHandle("post", request, response);
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        roleCheckHandle("get", request, response);
    }
}
