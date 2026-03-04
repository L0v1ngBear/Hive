package my.hive_back.module.tenant.model.vo;

import lombok.Data;
import my.hive_back.module.tenant.model.entity.Tenant;

@Data
public class TenantVo {

    private Long id;

    private String tenantName;
    private Integer status;
    private String contactPerson;
    private String contactPhone;

    public TenantVo(Tenant tenant) {
        this.id = tenant.getId();
        this.tenantName = tenant.getTenantName();
        this.status = tenant.getStatus();
        this.contactPerson = tenant.getContactPerson();
        this.contactPhone = tenant.getContactPhone();
    }
}
