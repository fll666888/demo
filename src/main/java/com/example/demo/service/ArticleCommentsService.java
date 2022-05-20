package com.example.demo.service;

import com.example.demo.entity.ArticleComments;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.ArticleCommentsVo;

import java.util.List;

/**
 * <p>
 * 评论表 服务类
 * </p>
 *
 * @author fll
 * @since 2022-04-19
 */
public interface ArticleCommentsService extends IService<ArticleComments> {

    /**
     * 嵌套评论回复互动
     * @param articleId
     * @param parentId
     * @return
     */
    List<ArticleCommentsVo> getArticleCommentsNested(Integer articleId, Long parentId);

    /**
     * 两层评论回复互动
     * @param articleId
     * @param parentId
     * @return
     */
    List<ArticleCommentsVo> getArticleCommentsTwoLayers(Integer articleId, Long parentId);

}
