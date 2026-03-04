package my.hive_back.module.tenant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import my.hive_back.module.tenant.mapper.TenantMapper;
import my.hive_back.module.tenant.model.dto.TenantInfoPageRequest;
import my.hive_back.module.tenant.model.vo.TenantVo;
import org.springframework.stereotype.Service;

@Service
public class TenantService {

    @Resource
    private TenantMapper tenantMapper;

    public TenantVo pageSearchTenant(TenantInfoPageRequest searchDTO) {
        IPage<TenantVo> page = new Page<TenantVo>();

    }
}
