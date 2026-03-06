package my.hive_back.module.order.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import my.hive_back.module.order.model.entity.SalesOrder;
import my.hive_back.module.order.model.dto.SalesOrderListRequest;

public interface SalesOrderServiceImpl {

    Page<SalesOrder> selectSalesOrder(SalesOrderListRequest request);
}
