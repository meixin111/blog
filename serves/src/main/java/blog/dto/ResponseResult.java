package blog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResponseResult<T> {
    private int code;

    @JsonProperty("msg")  // 序列化为msg
    private String message;

    private T data;

    // 构造方法
    public ResponseResult() {}

    public ResponseResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 静态工厂方法
    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(200, "success", data);
    }

    public static <T> ResponseResult<T> success(String message, T data) {
        return new ResponseResult<>(200, message, data);
    }

    public static <T> ResponseResult<T> error(int code, String message) {
        return new ResponseResult<>(code, message, null);
    }

    public static <T> ResponseResult<T> error(String message) {
        return error(500, message);
    }

    public static <T> ResponseResult<T> badRequest(String message) {
        return error(400, message);
    }

    public static <T> ResponseResult<T> unauthorized(String message) {
        return error(401, message);
    }

    public static <T> ResponseResult<T> forbidden(String message) {
        return error(403, message);
    }

    public static <T> ResponseResult<T> notFound(String message) {
        return error(404, message);
    }
}