package my.hive_back.module.sys.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import my.hive_back.module.sys.model.entity.SysUserRole;

import java.util.List;

public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    List<String> selectRoleCodesByUserAndTenant(Long userId, String tenantCode);

}
