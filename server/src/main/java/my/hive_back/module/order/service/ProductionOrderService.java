package my.hive_back.module.order.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import my.hive_back.common.exception.BusinessException;
import my.hive_back.module.order.mapper.ProductionOrderMapper;
import my.hive_back.module.order.mapper.ProductionOrderStatusLogMapper;
import my.hive_back.module.order.model.dto.ProductionOrderListRequest;
import my.hive_back.module.order.model.entity.ProductionOrder;
import my.hive_back.module.order.model.entity.ProductionOrderStatusLog;
import my.hive_back.module.order.service.impl.ProductionOrderServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductionOrderService implements ProductionOrderServiceImpl {

    @Resource
    private ProductionOrderMapper productionOrderMapper;

    @Resource
    private ProductionOrderStatusLogMapper statusLogMapper;

    /**
     * 查询生产订单列表
     * @param request
     * @return
     */
    @Override
    public Page<ProductionOrder> selectProductionOrder(ProductionOrderListRequest request) {

        LambdaQueryWrapper<ProductionOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductionOrder::getStatus, request.getStatus());
        queryWrapper.like(ProductionOrder::getOrderId, request.getKeyWord());
        queryWrapper.like(ProductionOrder::getCustomerName, request.getKeyWord());
        queryWrapper.like(ProductionOrder::getProjectName, request.getKeyWord());
        queryWrapper.orderByDesc(ProductionOrder::getOrderId);

        return productionOrderMapper.selectPage(new Page<>(request.getPageNum(), request.getPageSize()), queryWrapper);
    }

    /**
     * 查询生产订单详情
     * @param orderId
     * @return
     */
    @Override
    public ProductionOrder selectProductionOrderDetail(String orderId) {

        ProductionOrder productionOrder = productionOrderMapper.selectById(orderId);

        if (productionOrder == null) {
            throw new BusinessException(400, "订单不存在");
        }

        return productionOrder;
    }

    @Override
    public List<ProductionOrderStatusLog> selectOrderStausLog(@NotBlank String orderId) {

        LambdaQueryWrapper<ProductionOrderStatusLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductionOrderStatusLog::getOrderId, orderId);
        queryWrapper.orderByAsc(ProductionOrderStatusLog::getCreateTime);
        return statusLogMapper.selectList(queryWrapper);
    }
}
