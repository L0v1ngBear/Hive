package my.hive_back.module.sys.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import my.hive_back.module.sys.model.entity.SysRole;

import java.util.List;

public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<String> selectPermCodesByTenantAndRoleCodes(String tenantCode, List<String> roleCodes);
}
