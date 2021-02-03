package cn.wchihc.jwc.model.base;

/**
 * 数据包装类
 *
 * @param <T>
 */
public class Result<T> {
    //状态码
    private int code;
    //消息
    private String message;
    //具体数据内容
    private T data;

    public Result() {
    }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
