package my.hive_back.api.order;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import my.hive_back.common.dto.PageResultVO;
import my.hive_back.common.dto.ResultDTO;
import my.hive_back.module.order.model.dto.SalesOrderStatusRequest;
import my.hive_back.module.order.model.entity.SalesOrder;
import my.hive_back.module.order.model.dto.SalesOrderListRequest;
import my.hive_back.module.order.model.vo.SalesOrderVO;
import my.hive_back.module.order.service.SalesOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sales")
public class SalesOrderController {

    @Resource
    private SalesOrderService salesOrderService;

    @GetMapping("/orders/list")
    public ResultDTO<PageResultVO<SalesOrderVO>> selectSalesOrder(@RequestParam SalesOrderListRequest request) {
        Page<SalesOrder> page = salesOrderService.selectSalesOrder(request);
        PageResultVO<SalesOrderVO> pageResultVo = new PageResultVO<>() {
            {
                setCurrent(page.getCurrent());
                setSize(page.getSize());
                setTotal(page.getTotal());
                setPages(page.getPages());
                setData(page.getRecords().stream().map(order -> {
                    SalesOrderVO vo = new SalesOrderVO();
                    BeanUtils.copyProperties(order, vo);
                    return vo;
                }).toList());
            }
        };
        return ResultDTO.success(pageResultVo);
    }

    @GetMapping("/orders/detail/{orderId}")
    public ResultDTO<SalesOrderVO> getSalesOrderStatus(@PathVariable("orderId") String orderId) {
        // 查询订单状态
        SalesOrder order = salesOrderService.getByIdandTenantId(orderId);
        if (order == null) {
            return ResultDTO.fail(404, "订单不存在");
        }
        // copy属性
        SalesOrderVO statusVO = new SalesOrderVO();
        BeanUtils.copyProperties(order, statusVO);
        return ResultDTO.success(statusVO);
    }

    @PostMapping("/orders/status/{orderId}")
    public ResultDTO<SalesOrderVO> updateOrderStatus(@PathVariable("orderId") String orderId,
                                                           @Valid @RequestBody SalesOrderStatusRequest request) {

        SalesOrderVO statusVO = salesOrderService.updateOrderStatus(orderId, request);
        return ResultDTO.success(statusVO);
    }


    @GetMapping("/orders/expressInfo/{orderId}")
    public ResultDTO<SalesOrderVO> getSalesOrderExpressInfo(@PathVariable("orderId") String orderId) {
        // 查询订单状态
        SalesOrder order = salesOrderService.getByIdandTenantId(orderId);
        if (order == null) {
            return ResultDTO.fail(404, "订单不存在");
        }
        String expressCompany = order.getExpressCompany();
        String expressNo = order.getExpressNo();

        //TODO 对接物流信息接口

        // copy属性
        SalesOrderVO statusVO = new SalesOrderVO();
        BeanUtils.copyProperties(order, statusVO);
        return ResultDTO.success(statusVO);
    }
}