package com.example.demo.service.impl;

import com.example.demo.entity.ArticleComments;
import com.example.demo.entity.ArticleCommentsVo;
import com.example.demo.mapper.ArticleCommentsMapper;
import com.example.demo.service.ArticleCommentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author fll
 * @since 2022-04-19
 */
@Service
public class ArticleCommentsServiceImpl extends ServiceImpl<ArticleCommentsMapper, ArticleComments> implements ArticleCommentsService {

    @Autowired
    private ArticleCommentsMapper articleCommentsMapper;

    @Override
    public List<ArticleCommentsVo> getArticleCommentsNested(Integer articleId, Long parentId) {
        return articleCommentsMapper.getArticleCommentsList(articleId, parentId);
    }

    @Override
    public List<ArticleCommentsVo> getArticleCommentsTwoLayers(Integer articleId, Long parentId) {
        return findParent(articleCommentsMapper.getArticleCommentsList(articleId, parentId));
    }

    public List<ArticleCommentsVo> findParent(List<ArticleCommentsVo> comments) {
        for (ArticleCommentsVo comment : comments) {
            // 防止checkForComodification()，而建立一个新集合
            List<ArticleCommentsVo> children = new ArrayList<>();

            // 递归处理子级的回复，即回复内有回复
            findChildren(comment, children);

            // 将递归处理后的集合放回父级的子级中
            comment.setChildCommentsList(children);
        }
        return comments;
    }

    public void findChildren(ArticleCommentsVo parent, List<ArticleCommentsVo> children) {
        // 找出直接子级
        List<ArticleCommentsVo> comments = parent.getChildCommentsList();

        // 遍历直接子级的子级
        for (ArticleCommentsVo comment : comments) {

            // 若非空，则还有子级，递归
            if (!comment.getChildCommentsList().isEmpty()) {
                findChildren(comment, children);
            }

            // 已经到了最底层的嵌套关系，将该回复放入新建立的集合
            children.add(comment);

            // 容易忽略的地方：将相对底层的子级放入新建立的集合之后，
            // 则表示解除了嵌套关系，对应的其父级的子级应该设为空。
            comment.setChildCommentsList(new ArrayList<>());
        }
    }

}
