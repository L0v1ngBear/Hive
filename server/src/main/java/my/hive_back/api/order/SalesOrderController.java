package my.hive_back.api.order;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import my.hive_back.common.dto.PageResultVO;
import my.hive_back.common.dto.ResultDTO;
import my.hive_back.module.order.OrderStatusEnum;
import my.hive_back.module.order.model.dto.SalesOrderStatusRequest;
import my.hive_back.module.order.model.entity.SalesOrder;
import my.hive_back.module.order.model.dto.SalesOrderListRequest;
import my.hive_back.module.order.model.vo.SalesOrderListVO;
import my.hive_back.module.order.model.vo.SalesOrderStatusVO;
import my.hive_back.module.order.service.SalesOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sales")
public class SalesOrderController {

    @Resource
    private SalesOrderService salesOrderService;

    @GetMapping("/orders/list")
    public ResultDTO<PageResultVO<SalesOrderListVO>> selectSalesOrder(@RequestParam SalesOrderListRequest request) {
        Page<SalesOrder> page = salesOrderService.selectSalesOrder(request);
        PageResultVO<SalesOrderListVO> pageResultVo = new PageResultVO<>() {
            {
                setCurrent(page.getCurrent());
                setSize(page.getSize());
                setTotal(page.getTotal());
                setPages(page.getPages());
                setData(page.getRecords().stream().map(order -> {
                    SalesOrderListVO vo = new SalesOrderListVO();
                    BeanUtils.copyProperties(order, vo);
                    return vo;
                }).toList());
            }
        };
        return ResultDTO.success(pageResultVo);
    }

    @GetMapping("/orders/{orderId}")
    public ResultDTO<SalesOrderStatusVO> getSalesOrderStatus(@PathVariable("orderId") String orderId) {
        // 查询订单状态
        SalesOrder order = salesOrderService.getByIdandTenantId(orderId);
        if (order == null) {
            return ResultDTO.fail(404, "订单不存在");
        }
        // copy属性
        SalesOrderStatusVO statusVO = new SalesOrderStatusVO();
        BeanUtils.copyProperties(order, statusVO);
        return ResultDTO.success(statusVO);
    }

    @PostMapping("/orders/{orderId}/status")
    @Transactional(rollbackFor = Exception.class)
    public ResultDTO<SalesOrderStatusVO> updateOrderStatus(@PathVariable("orderId") String orderId,
                                                           @Valid @RequestBody SalesOrderStatusRequest request) {

        // 校验已发货订单是否提供物流信息
        if (OrderStatusEnum.SHIPPED.getName().equals(request.getStatus())) {
            if (request.getExpressInfo() == null ||
                    request.getExpressInfo().getExpressCompany().isBlank() ||
                    request.getExpressInfo().getExpressNo().isBlank()) {
                return ResultDTO.fail(401, "已发货订单必须提供物流信息");
            }
        }

        // 查询订单是否存在
        // for update 加行锁，防止并发更新
        SalesOrder order = salesOrderService.getByIdandTenantId(orderId);
        if (order == null) {
            return ResultDTO.fail(404, "订单不存在");
        }

        // 校验订单状态转换是否有效
        String oldStatus = order.getStatus();
        String newStatus = request.getStatus();
        if (!OrderStatusEnum.canFlowTo(oldStatus, newStatus)) {
            return ResultDTO.fail(401, "订单状态转换无效");
        }

        // 更新订单状态
        // 乐观锁控制
        order.setStatus(newStatus);
        order.setExpressCompany(request.getExpressInfo().getExpressCompany());
        order.setExpressNo(request.getExpressInfo().getExpressNo());
        salesOrderService.updateOrderStatus(order, oldStatus);

        // copy属性
        SalesOrderStatusVO vo = new SalesOrderStatusVO();
        BeanUtils.copyProperties(order, vo);

        // 返回更新后的订单状态
        return ResultDTO.success(vo);
    }


    @GetMapping("/orders/expressInfo/{orderId}")
    public ResultDTO<SalesOrderStatusVO> getSalesOrderExpressInfo(@PathVariable("orderId") String orderId) {
        // 查询订单状态
        SalesOrder order = salesOrderService.getByIdandTenantId(orderId);
        if (order == null) {
            return ResultDTO.fail(404, "订单不存在");
        }
        String expressCompany = order.getExpressCompany();
        String expressNo = order.getExpressNo();

        //TODO 对接物流信息接口

        // copy属性
        SalesOrderStatusVO statusVO = new SalesOrderStatusVO();
        BeanUtils.copyProperties(order, statusVO);
        return ResultDTO.success(statusVO);
    }
}