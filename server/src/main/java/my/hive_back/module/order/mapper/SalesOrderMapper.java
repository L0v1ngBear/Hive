package my.hive_back.module.order.mapper;

import my.hive_back.module.order.model.dto.SalesOrderStatusRequest;
import my.hive_back.module.order.model.entity.SalesOrder;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

public interface SalesOrderMapper extends BaseMapper<SalesOrder> {

    @Update("UPDATE sales_order SET status = #{request.status} " +
            "AND express_company = #{request.expressCompany} " +
            "AND express_no = #{request.expressNo} " +
            "WHERE order_id = #{order.orderId} " +
            "AND status = #{order.status} " +
            "AND tenant_id = #{TENANTCONTEXTHOLDER.getTenantId()}")
    int updateStatus(SalesOrder order, SalesOrderStatusRequest request);
}
