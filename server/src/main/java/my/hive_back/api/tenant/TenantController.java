package my.hive_back.api.tenant;

import my.hive_back.common.dto.ResultDTO;
import my.hive_back.module.tenant.model.dto.TenantInfoPageRequest;
import my.hive_back.module.tenant.model.vo.TenantVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.validation.Valid;
@Controller
@RequestMapping("/tenant")
public class TenantController {

    @GetMapping("/page-search")
    public ResultDTO<TenantVo> pageSearchTenant(@Valid @RequestBody TenantInfoPageRequest searchDTO) {

    }
}
