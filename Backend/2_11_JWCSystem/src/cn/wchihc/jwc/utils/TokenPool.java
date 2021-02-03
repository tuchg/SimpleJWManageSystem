package cn.wchihc.jwc.utils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Token池
 * 负责维护Token的生成,校验
 */
public class TokenPool {
    //线程安全
    private static final Map<String, Integer> tokens = new ConcurrentHashMap<>();


    private static final String[] roles = new String[]{"admin", "student", "teacher"};


    /**
     * 根据用户组生成Token
     *
     * @param role 用户组
     * @return null生成失败
     */
    public static synchronized String genToken(String role, int userId) {
        for (String r : roles) {
            String str = role.trim();
            if (str.equals(r)) {
                String token = str + "-" + UUID.randomUUID().toString().replace("-", "");
                tokens.put(token, userId);
                return token;
            }
        }
        return null;
    }

    /**
     * 验证token是否有效
     *
     * @param token token
     * @return 从token中提取的角色
     */
    public static synchronized String validateToken(String token) {
        return tokens.containsKey(token) ? token.substring(0, token.indexOf("-")) : null;
    }

    /**
     * 根据token获取当前用户
     *
     * @param token
     * @return
     */
    public static synchronized Integer getId(String token) {
        return tokens.get(token);
    }

    /**
     * 清除指定token
     *
     * @param token
     */
    public static synchronized void popToken(String token) {
        tokens.remove(token);
    }

    /**
     * 清空tokens
     */
    public static synchronized void clearTokens() {
        tokens.clear();
    }

}
