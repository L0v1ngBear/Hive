package my.hive_back.api.inventory;

import jakarta.annotation.Resource;
import my.hive_back.common.dto.ResultDTO;
import my.hive_back.module.inventory.model.entity.InventoryStatics;
import my.hive_back.module.inventory.model.vo.InventoryOverViewVO;
import my.hive_back.module.inventory.service.InventoryService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
@Validated
public class InventoryController {

    @Resource
    private InventoryService inventoryService;

    @GetMapping("/list")
    public ResultDTO<InventoryOverViewVO> listInventory() {
        InventoryStatics inventoryStatics = inventoryService.selectInventoryStatics();

    }
}
