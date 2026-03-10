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

    @GetMapping("/orders/detail/{orderId}")
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

    @PostMapping("/orders/status/{orderId}")
    public ResultDTO<SalesOrderStatusVO> updateOrderStatus(@PathVariable("orderId") String orderId,
                                                           @Valid @RequestBody SalesOrderStatusRequest request) {

        SalesOrderStatusVO statusVO = salesOrderService.updateOrderStatus(orderId, request);
        return ResultDTO.success(statusVO);
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