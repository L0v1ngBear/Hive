package my.hive_back.module.tenant.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;


@TableName("tenant")
public class Tenant {

    private Long id;

    private String tenantCode;

    private String tenantName;

    private String contactPerson;

    private String contactPhone;

    private String password;

    private Integer status;

    private Long creator;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer deleted;
}
