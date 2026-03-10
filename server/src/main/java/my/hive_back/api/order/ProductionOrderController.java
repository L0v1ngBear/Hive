package my.hive_back.api.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import my.hive_back.common.dto.PageResultVO;
import my.hive_back.common.dto.ResultDTO;
import my.hive_back.module.order.model.dto.ProductionOrderListRequest;
import my.hive_back.module.order.model.entity.ProductionOrder;
import my.hive_back.module.order.model.vo.ProductionOrderVO;
import my.hive_back.module.order.service.ProductionOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * 生产订单控制器
 *
 */
@RestController
@RequestMapping("/production")
@Validated
public class ProductionOrderController {

    @Resource
    private ProductionOrderService productionOrderService;

    /**
     * 生产订单列表查询
     * 补充：@Valid 触发复杂对象内部校验
     */
    @GetMapping("/orders/list")
    public ResultDTO<PageResultVO<ProductionOrderVO>> selectProductionOrder(
            @RequestParam ProductionOrderListRequest request) {

        IPage<ProductionOrder> page = productionOrderService.selectProductionOrder(request);
        PageResultVO<ProductionOrderVO> pageResultVO = new PageResultVO<>() {
            {
                setCurrent(page.getCurrent());
                setSize(page.getSize());
                setTotal(page.getTotal());
                setPages(page.getPages());
                setData(page.getRecords().stream().map(order -> {
                    ProductionOrderVO vo = new ProductionOrderVO();
                    BeanUtils.copyProperties(order, vo);
                    return vo;
                }).collect(Collectors.toList()));
            }
        };
        return ResultDTO.success(pageResultVO);
    }

    /**
     * 生产订单详情查询
     * 补充：orderId 非空 + 格式校验
     */
    @GetMapping("/orders/detail/{orderId}")
    public ResultDTO<ProductionOrderVO> getProductionOrderDetail(
            // 1. 非空校验：订单ID不能为空
            @NotBlank(message = "生产订单ID不能为空")
            @PathVariable("orderId") String orderId) {

        //TODO 详情查询
        ProductionOrder order = productionOrderService.selectProductionOrderDetail(orderId);
        // 补充：空值判断（避免NPE）
        if (order == null) {
            return ResultDTO.fail(404, "生产订单不存在");
        }
        ProductionOrderVO vo = new ProductionOrderVO();
        BeanUtils.copyProperties(order, vo);
        return ResultDTO.success(vo);
    }
}