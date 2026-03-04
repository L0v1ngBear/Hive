package my.hive_back.common.exception;

import my.hive_back.common.dto.ResultDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 全局异常处理器（适配jakarta.validation的@Valid校验异常）
 * RestControllerAdvice：捕获所有@RestController的异常
 * Slf4j：日志打印
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理@Valid + @RequestBody的参数校验失败（最常用）
     * 场景：POST/PUT请求，JSON入参校验失败
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 返回400状态码
    public ResultDTO<Void> handleRequestBodyValidException(MethodArgumentNotValidException e) {
        // 获取校验失败的所有字段信息
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        // 拼接错误信息：字段名 + 错误提示
        String errorMsg = fieldErrors.stream()
                .map(error -> String.format("字段[%s]：%s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining("；"));

        // 打印日志（便于排查问题）
        log.error("JSON参数校验失败：{}", errorMsg, e);

        // 返回统一格式的错误结果（适配你的ResultDTO）
        return ResultDTO.fail(400, errorMsg);
    }

    /**
     * 处理@Valid + @RequestParam/@PathVariable的参数校验失败
     * 场景：GET请求，路径/查询参数校验失败
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultDTO<Void> handleRequestParamValidException(ConstraintViolationException e) {
        // 获取所有参数校验失败信息
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();

        // 拼接错误信息：参数名 + 错误提示
        String errorMsg = violations.stream()
                .map(violation -> String.format("参数[%s]：%s", violation.getPropertyPath(), violation.getMessage()))
                .collect(Collectors.joining("；"));

        log.error("路径/查询参数校验失败：{}", errorMsg, e);
        return ResultDTO.fail(400, errorMsg);
    }

    /**
     * 兜底异常处理（捕获所有未定义的异常）
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultDTO<Void> handleGlobalException(Exception e) {
        log.error("系统异常", e);
        return ResultDTO.fail(500, "服务器内部错误，请稍后重试");
    }
}