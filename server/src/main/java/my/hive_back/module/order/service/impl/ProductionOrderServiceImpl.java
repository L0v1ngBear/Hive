package my.hive_back.module.order.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.validation.constraints.NotBlank;
import my.hive_back.module.order.model.dto.ProductionOrderListRequest;
import my.hive_back.module.order.model.entity.ProductionOrder;
import my.hive_back.module.order.model.entity.ProductionOrderStatusLog;

import java.util.List;

public interface ProductionOrderServiceImpl {

    IPage<ProductionOrder> selectProductionOrder(ProductionOrderListRequest request);

    ProductionOrder selectProductionOrderDetail(String orderId);

    List<ProductionOrderStatusLog> selectOrderStausLog(@NotBlank String orderId);
}
