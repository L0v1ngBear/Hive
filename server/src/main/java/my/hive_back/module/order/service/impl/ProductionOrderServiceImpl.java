package my.hive_back.module.order.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import my.hive_back.module.order.model.dto.ProductionOrderListRequest;
import my.hive_back.module.order.model.entity.ProductionOrder;

public interface ProductionOrderServiceImpl {

    IPage<ProductionOrder> selectProductionOrder(ProductionOrderListRequest request);
}
