package my.hive_back.module.inventory.model.vo;

import lombok.Data;

@Data
public class InventoryOverViewVO {

    private Long totalRollCount;

    private Float totalMeters;

    private Float totalInMeters;

    private Float totalOutMeters;

}
