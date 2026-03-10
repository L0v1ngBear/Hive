package my.hive_back.common.interceptor;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import my.hive_back.common.context.TenantContextHolder;
import my.hive_back.module.tenant.mapper.TenantMapper;
import my.hive_back.module.tenant.model.entity.Tenant;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TenantInterceptor implements HandlerInterceptor {

    @Resource
    private TenantMapper tenantMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String tenantId = request.getHeader("Tenant-id");

        // 校验租户ID是否存在且有效
        if (tenantId == null || tenantId.trim().isEmpty()) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            // 返回标准化错误信息
            String errorMsg = "{\"code\":\"400\",\"msg\":\"无权限\",\"data\":null}";
            response.getWriter().write(errorMsg);
            return false; // 拦截请求，不继续执行
        }

        Tenant tenant = tenantMapper.selectById(Long.parseLong(tenantId));

        if (tenant == null) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            // 返回标准化错误信息
            String errorMsg = "{\"code\":\"400\",\"msg\":\"无权限\",\"data\":null}";
            response.getWriter().write(errorMsg);
            return false; // 拦截请求，不继续执行
        }

        if (StringUtils.hasText(tenantId)) {
            TenantContextHolder.setTenantId(Long.parseLong(tenantId));
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        TenantContextHolder.clear();
    }
}
