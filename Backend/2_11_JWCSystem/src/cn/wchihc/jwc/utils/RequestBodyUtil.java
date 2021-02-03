package cn.wchihc.jwc.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class RequestBodyUtil {
    /**
     * 获取 JSON字符串
     *
     * @param request
     * @return
     */
    public static String getContentBody(HttpServletRequest request) {
        try {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);

            return responseStrBuilder.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
