package my.hive_back.module.order.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductionOrderDetailVO {

    private String orderId;


    private String status;

    private String model;

    private String fabric;

    private BigDecimal weight;

    private BigDecimal width;

    private String color;

    private Integer quantity;

    private BigDecimal price;

    private BigDecimal totalAmount;

    private Integer process;

    private String customerId;

    private String customerName;

    private String projectName;

    private String contactPhone;

    private LocalDateTime deliveryDate;

}
