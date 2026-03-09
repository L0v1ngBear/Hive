package my.hive_back.module.order;

import lombok.Getter;

public enum SalesOrderStatusEnum {
    /** 待支付 */
    PENDING_PAYMENT("pending_pay","待收款"),
    /** 待发货 */
    PENDING_SHIPMENT("pending_ship","待发货"),
    /** 已发货 */
    SHIPPED("shipped","已发货"),
    /** 已完成 */
    COMPLETED("completed","已完成"),
    /** 已取消 */
    CANCELLED("cancelled","已取消");

    @Getter
    private String name;
    private String desc;

    private SalesOrderStatusEnum(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public static SalesOrderStatusEnum getByName(String name) {
        for (SalesOrderStatusEnum status : SalesOrderStatusEnum.values()) {
            if (status.getName().equals(name)) {
                return status;
            }
        }
        return null;
    }
}
