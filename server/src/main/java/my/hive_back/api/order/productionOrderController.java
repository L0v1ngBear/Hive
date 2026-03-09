package my.hive_back.api.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import my.hive_back.common.dto.PageResultVo;
import my.hive_back.common.dto.ResultDTO;
import my.hive_back.module.order.model.dto.ProductionOrderListRequest;
import my.hive_back.module.order.model.vo.ProductionOrderListVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/production")
public class productionOrderController {

    @GetMapping("/orders/list")
    public ResultDTO<PageResultVo<ProductionOrderListVO>> selectProductionOrder(
            @RequestParam ProductionOrderListRequest request) {

        IPage<>
    }
}
