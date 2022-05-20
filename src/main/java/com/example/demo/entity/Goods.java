package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 商品表
 * </p>
 *
 * @author fll
 * @since 2022-03-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("goods")
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Integer id;

    /**
     * 商品名称
     */
    @TableField("name")
    private String name;

    /**
     * 库存
     */
    @TableField("inventory")
    private Long inventory;

    /**
     * 是否删除 0:不删除 1:删除
     */
    @TableField("del")
    private Integer del;

    /**
     * 创建人
     */
    @TableField("createAt")
    private String createat;

    /**
     * 创建时间
     */
    @TableField("createDate")
    private LocalDateTime createdate;

    /**
     * 修改人
     */
    @TableField("modifyAt")
    private String modifyat;

    /**
     * 修改时间
     */
    @TableField("modifyDate")
    private LocalDateTime modifydate;


}
