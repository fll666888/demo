package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(value = { "handler" })//mybatis懒加载序列化的时候会生成一个handler的空bean导致报错，这个注解的作用是json序列化时忽略bean中的一些属性.
@Data
public class ArticleCommentsVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 评论id
	 */
	private Long id;

	/**
	 * 文章id
	 */
	private Integer articleId;

	/**
	 * 父评论id
	 */
	private Long parentId;

	/**
	 * 用户id
	 */
	private Long userId;

	/**
	 * 是否自身
	 */
	private boolean selfFlag;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 点赞数量
	 */
	private Integer dzQuantity;

	/**
     * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone="GMT+8")
	private Date createDate;

	List<ArticleCommentsVo> childCommentsList;

}
