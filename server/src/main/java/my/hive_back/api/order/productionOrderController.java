package my.hive_back.api.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.annotation.Resource;
import my.hive_back.common.dto.PageResultVO;
import my.hive_back.common.dto.ResultDTO;
import my.hive_back.module.order.model.dto.ProductionOrderListRequest;
import my.hive_back.module.order.model.entity.ProductionOrder;
import my.hive_back.module.order.model.vo.ProductionOrderListVO;
import my.hive_back.module.order.service.ProductionOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/production")
public class productionOrderController {

    @Resource
    private ProductionOrderService productionOrderService;

    @GetMapping("/orders/list")
    public ResultDTO<PageResultVO<ProductionOrderListVO>> selectProductionOrder(
            @RequestParam ProductionOrderListRequest request) {

        IPage<ProductionOrder> page = productionOrderService.selectProductionOrder(request);
        PageResultVO<ProductionOrderListVO> pageResultVO = new PageResultVO<>() {
            {
                setCurrent(page.getCurrent());
                setSize(page.getSize());
                setTotal(page.getTotal());
                setPages(page.getPages());
                setData(page.getRecords().stream().map(order -> {
                    ProductionOrderListVO vo = new ProductionOrderListVO();
                    BeanUtils.copyProperties(order, vo);
                    return vo;
                }).collect(Collectors.toList()));
            }
        };
        return ResultDTO.success(pageResultVO);
    }
}
