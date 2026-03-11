package my.hive_back.module.inventory.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import my.hive_back.module.inventory.model.entity.InventoryStatics;
import my.hive_back.module.inventory.model.mapper.InventoryStaticsMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Service
public class InventoryService {

    @Resource
    private InventoryStaticsMapper staticsMapper;

    @Resource
    private InventoryMapper inventoryMapper;

    public InventoryStatics selectInventoryStatics() {

        // 构造当前日期的时间范围
        LocalDate today = LocalDate.now();
        LocalDateTime startTime = today.atStartOfDay(); // 当天00:00:00
        LocalDateTime endTime = today.plusDays(1).atStartOfDay().minusNanos(1); // 当天23:59:59.999999999

        // 数据来源于定时任务的统计
        // TODO 统计数据
        LambdaQueryWrapper<InventoryStatics> wrapper = new LambdaQueryWrapper<>();
        wrapper.between(InventoryStatics::getCreateTime, startTime, endTime);

        return staticsMapper.selectOne(wrapper);
    }
}
