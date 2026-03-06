package my.hive_back.module.order.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import my.hive_back.common.dto.ResultDTO;
import my.hive_back.module.order.mapper.SalesOrderMapper;
import my.hive_back.module.order.model.SalesOrder;
import my.hive_back.module.order.model.dto.SalesOrderListRequest;
import my.hive_back.module.order.service.impl.SalesOrderServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SalesOrderService implements SalesOrderServiceImpl {

    @Resource
    private SalesOrderMapper salesOrderMapper;

    @Override
    public Page<SalesOrder> selectSalesOrder(SalesOrderListRequest request) {

        LambdaQueryWrapper<SalesOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SalesOrder::getStatus, request.getStatus());

        // 数据量大时模糊查询瓶颈
        queryWrapper.like(SalesOrder::getOrderId, request.getKeyWord());
        queryWrapper.like(SalesOrder::getCustomerName, request.getKeyWord());
        queryWrapper.orderByDesc(SalesOrder::getCreateTime);
        return salesOrderMapper.selectPage(new Page<>(request.getPageNum(), request.getPageSize()), queryWrapper);
    }
}
