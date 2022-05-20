package com.example.demo.mapper;

import com.example.demo.entity.ArticleComments;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.ArticleCommentsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 评论表 Mapper 接口
 * </p>
 *
 * @author fll
 * @since 2022-04-19
 */
@Repository
@Mapper
public interface ArticleCommentsMapper extends BaseMapper<ArticleComments> {

    List<ArticleCommentsVo> getArticleCommentsList(@Param("articleId") Integer articleId, @Param("parentId") Long parentId);

}
