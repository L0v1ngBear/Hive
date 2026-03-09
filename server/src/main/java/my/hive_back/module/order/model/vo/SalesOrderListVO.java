package my.hive_back.module.order.model.vo;

import lombok.Data;
import my.hive_back.module.order.model.entity.SalesOrder;

import java.math.BigDecimal;

@Data
public class SalesOrderListVO {
    private String orderId;
    private String status;
    private String customerName;
    private String goodsDesc;
    private BigDecimal totalAmount;
    private Integer totalQuantity;
    private String deliveryDate;
    private String expressCompany;
    private String expressNo;
    private String createTime;

    public SalesOrderListVO(SalesOrder salesOrder) {
        this.orderId = salesOrder.getOrderId();
        this.status = salesOrder.getStatus();
        this.customerName = salesOrder.getCustomerName();
        this.goodsDesc = salesOrder.getGoodsDesc();
        this.totalAmount = salesOrder.getTotalAmount();
        this.totalQuantity = salesOrder.getTotalQuantity();
        this.deliveryDate = salesOrder.getDeliveryDate();
        this.expressCompany = salesOrder.getExpressCompany();
        this.expressNo = salesOrder.getExpressNo();
        this.createTime = salesOrder.getCreateTime().toString();
    }
}
