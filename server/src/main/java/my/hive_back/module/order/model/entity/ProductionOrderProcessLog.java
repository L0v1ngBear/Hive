package my.hive_back.module.order.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

/**
 * 生产订单工序日志实体类
 *
 *
 */
@Data
@TableName("production_order_process_log")
public class ProductionOrderProcessLog {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 生产订单编号
     */
    @TableField("order_id")
    private String orderId;

    /**
     * 工序：0-整经，1-浆纱，2-织造，3-验布，4-卷布
     */
    @TableField("process")
    private Integer process;

    /**
     * 工序文字描述
     */
    @TableField("process_text")
    private String processText;

    /**
     * 工序完成时间
     */
    @TableField("complete_time")
    private Date completeTime;

    /**
     * 工序备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 操作人
     */
    @TableField("operator")
    private String operator;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
}