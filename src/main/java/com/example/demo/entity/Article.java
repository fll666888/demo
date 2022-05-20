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
 * 文章表
 * </p>
 *
 * @author fll
 * @since 2022-04-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("article")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文章id
     */
    @TableId("id")
    private Integer id;

    /**
     * 标题
     */
    @TableField("title")
    private String title;

    /**
     * 内容
     */
    @TableField("content")
    private String content;

    /**
     * 评论数量
     */
    @TableField("pl_quantity")
    private Integer plQuantity;

    /**
     * 点赞数量
     */
    @TableField("dz_quantity")
    private Integer dzQuantity;

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
