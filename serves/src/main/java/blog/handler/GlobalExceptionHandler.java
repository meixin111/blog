package blog.handler;

import blog.dto.ResponseResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseResult<?> handleAuthenticationException(AuthenticationException e) {
        return ResponseResult.unauthorized(e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseResult<?> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseResult.forbidden("没有权限访问");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult<?> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("参数错误");
        return ResponseResult.badRequest(message);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseResult<?> handleRuntimeException(RuntimeException e) {
        return ResponseResult.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult<?> handleException(Exception e) {
        e.printStackTrace();
        return ResponseResult.error("服务器内部错误");
    }
}