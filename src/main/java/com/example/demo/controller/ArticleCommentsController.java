package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.demo.entity.Article;
import com.example.demo.entity.ArticleComments;
import com.example.demo.service.ArticleCommentsService;
import com.example.demo.service.ArticleService;
import com.example.demo.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 评论表
 *
 * @author fll
 * @since 2022-04-19
 */
@RestController
@RequestMapping("/articlecomments")
@Api(value = "articlecomments", tags = "评论表管理")
public class ArticleCommentsController {

    @Autowired
    private ArticleCommentsService articleCommentsService;

    @Autowired
    private ArticleService articleService;

    //手动事务
    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;
    @Autowired
    private TransactionDefinition transactionDefinition;

    @GetMapping("/getCommentsList")
    @ApiOperation(value = "根据文章id查询评论列表")
    public R getCommentsList(Integer type, Integer articleId, Long parentId) {
        if(type == 1) {
            return R.ok(articleCommentsService.getArticleCommentsNested(articleId, parentId), "成功");
        } else {
            return R.ok(articleCommentsService.getArticleCommentsTwoLayers(articleId, parentId), "成功");
        }
    }

    @PostMapping("/releaseComments")
    @ApiOperation(value = "发布评论")
    @Transactional
    public R releaseComments(@RequestBody ArticleComments articleComments) {
        articleComments.setContent(articleComments.getContent().trim());
        articleComments.setCreatedate(LocalDateTime.now());
        boolean result = articleCommentsService.save(articleComments);
        if(result) {
            UpdateWrapper<Article> updateWrapper = new UpdateWrapper<>();
            updateWrapper.setSql("pl_quantity = pl_quantity + 1");
            updateWrapper.eq("id", articleComments.getArticleId());
            articleService.update(updateWrapper);
        }
        return R.ok(articleComments, "成功");
    }

    @PostMapping("/replyComments")
    @ApiOperation(value = "回复评论")
    @Transactional
    public R replyComments(@RequestBody ArticleComments articleComments) {
        if(articleComments.getParentId() == null) {
            return R.failed("父评论id不能为空");
        }
        articleComments.setContent(articleComments.getContent().trim());
        articleComments.setCreatedate(LocalDateTime.now());
        boolean result = articleCommentsService.save(articleComments);
        if(result) {
            UpdateWrapper<Article> updateWrapper = new UpdateWrapper<>();
            updateWrapper.setSql("pl_quantity = pl_quantity + 1");
            updateWrapper.eq("id", articleComments.getArticleId());
            articleService.update(updateWrapper);
        }
        return R.ok(articleComments, "成功");
    }

    @DeleteMapping("/deleteComments")
    @ApiOperation(value = "删除评论")
    public synchronized R deleteComments(Long commentsId) {
        //手动开启事务
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            QueryWrapper<ArticleComments> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", commentsId);
            queryWrapper.eq("del", 0);
            ArticleComments articleComments = articleCommentsService.getOne(queryWrapper);
            if(articleComments != null) {
                UpdateWrapper<ArticleComments> updateWrapper = new UpdateWrapper();
                updateWrapper.set("del", 1);
                updateWrapper.set("modifyDate", LocalDateTime.now());
                updateWrapper.eq("id", commentsId);
                boolean result = articleCommentsService.update(updateWrapper);
                if(result) {
                    UpdateWrapper<Article> updateWrapperPl = new UpdateWrapper<>();
                    updateWrapperPl.setSql("pl_quantity = pl_quantity - 1");
                    updateWrapperPl.eq("id", articleComments.getArticleId());
                    if(!articleService.update(updateWrapperPl)) {
                        //手动回滚事务
                        dataSourceTransactionManager.rollback(transactionStatus);
                        return R.failed();
                    }
                    QueryWrapper<ArticleComments> queryWrapper2 = new QueryWrapper<>();
                    queryWrapper2.select("id");
                    queryWrapper2.eq("parent_id", commentsId);
                    queryWrapper2.eq("del", 0);
                    List<ArticleComments> list = articleCommentsService.list(queryWrapper2);
                    if(list != null && list.size() > 0) {
                        list.stream().forEach(e -> { e.setDel(1); e.setModifydate(LocalDateTime.now()); });
                        boolean result2 = articleCommentsService.updateBatchById(list);
                        if(result2) {
                            UpdateWrapper<Article> updateWrapperPl2 = new UpdateWrapper<>();
                            updateWrapperPl2.setSql("pl_quantity = pl_quantity - " + list.size());
                            updateWrapperPl2.eq("id", articleComments.getArticleId());
                            if(!articleService.update(updateWrapperPl2)) {
                                //手动回滚事务
                                dataSourceTransactionManager.rollback(transactionStatus);
                                return R.failed();
                            }
                        } else {
                            //手动回滚事务
                            dataSourceTransactionManager.rollback(transactionStatus);
                            return R.failed();
                        }
                    }
                } else {
                    //手动回滚事务
                    dataSourceTransactionManager.rollback(transactionStatus);
                    return R.failed();
                }
                //手动提交事务
                dataSourceTransactionManager.commit(transactionStatus);
                return R.ok("成功");
            } else {
                return R.ok("已删除");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //手动回滚事务
            dataSourceTransactionManager.rollback(transactionStatus);
            return R.failed();
        }
    }

}
