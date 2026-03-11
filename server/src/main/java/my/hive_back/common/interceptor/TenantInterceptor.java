package my.hive_back.common.interceptor;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import my.hive_back.common.context.TenantContextHolder;
import my.hive_back.common.dto.ResultDTO;
import my.hive_back.module.tenant.mapper.TenantMapper;
import my.hive_back.module.tenant.model.entity.Tenant;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TenantInterceptor implements HandlerInterceptor {

    @Resource
    private TenantMapper tenantMapper;

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String tenantCodeStr = request.getHeader("Tenant-Code");

        // 1. 租户ID为空：返回HTTP 400 + 业务400
        if (StringUtils.isBlank(tenantCodeStr)) {
            writeErrorResponse(response, HttpStatus.BAD_REQUEST, 400, "无权限");
            return false;
        }

        // TODO 租户id格式校验

        // 3. 租户不存在：返回HTTP 403（禁止访问） + 业务403（更精准）
        Tenant tenant = tenantMapper.selectById(tenantCodeStr);
        if (tenant == null) {
            writeErrorResponse(response, HttpStatus.FORBIDDEN, 403, "无权限访问");
            return false;
        }

        TenantContextHolder.setTenantCode(tenantCodeStr);
        return true;
    }

    /**
     * 统一写入错误响应（支持自定义HTTP状态码）
     */
    private void writeErrorResponse(HttpServletResponse response, HttpStatus httpStatus,
                                    Integer bizCode, String msg) throws Exception {
        ResultDTO<Void> errorResult = ResultDTO.fail(bizCode, msg);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(httpStatus.value()); // 设置HTTP状态码
        try (var writer = response.getWriter()) {
            objectMapper.writeValue(writer, errorResult);
            writer.flush();
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        TenantContextHolder.clear();
    }
}
