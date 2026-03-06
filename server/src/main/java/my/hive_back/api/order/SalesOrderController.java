package my.hive_back.api.order;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import my.hive_back.common.dto.PageResultVo;
import my.hive_back.common.dto.ResultDTO;
import my.hive_back.module.order.model.SalesOrder;
import my.hive_back.module.order.model.dto.SalesOrderListRequest;
import my.hive_back.module.order.model.vo.SalesOrderListVo;
import my.hive_back.module.order.service.SalesOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/sales")
public class SalesOrderController {

    @Resource
    private SalesOrderService salesOrderService;

    @GetMapping("/orders")
    public ResultDTO<PageResultVo<SalesOrderListVo>> selectSalesOrder(@RequestParam SalesOrderListRequest request) {
        Page<SalesOrder> page = salesOrderService.selectSalesOrder(request);
    }
}
