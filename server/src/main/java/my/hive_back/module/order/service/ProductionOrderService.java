package my.hive_back.module.order.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import my.hive_back.common.exception.BusinessException;
import my.hive_back.module.order.mapper.ProductionOrderMapper;
import my.hive_back.module.order.model.dto.ProductionOrderListRequest;
import my.hive_back.module.order.model.entity.ProductionOrder;
import my.hive_back.module.order.model.vo.ProductionOrderVO;
import my.hive_back.module.order.service.impl.ProductionOrderServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ProductionOrderService implements ProductionOrderServiceImpl {

    @Resource
    private ProductionOrderMapper productionOrderMapper;

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
    public ProductionOrder selectProductionOrderDetail(String orderId) {
        if (orderId == null) {
            throw new BusinessException(400, "参数不合法");
        }
    }
}
