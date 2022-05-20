package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.demo.entity.Article;
import com.example.demo.entity.ArticleComments;
import com.example.demo.entity.ArticleGiveALike;
import com.example.demo.service.ArticleCommentsService;
import com.example.demo.service.ArticleGiveALikeService;
import com.example.demo.service.ArticleService;
import com.example.demo.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 点赞表
 *
 * @author fll
 * @since 2022-04-19
 */
@RestController
@RequestMapping("/articlegivealike")
@Api(value = "articlegivealike", tags = "点赞表管理")
public class ArticleGiveALikeController {

    @Autowired
    private ArticleGiveALikeService articleGiveALikeService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleCommentsService articleCommentsService;

    @PostMapping("/giveALike")
    @ApiOperation(value = "点赞文章和文章评论")
    @Transactional
    public R giveALike(@RequestBody ArticleGiveALike articleGiveALike) {
        try {
            articleGiveALike.setCreatedate(LocalDateTime.now());
            articleGiveALikeService.save(articleGiveALike);
            if(articleGiveALike.getType() == 1) {//文章
                UpdateWrapper<Article> updateWrapper = new UpdateWrapper<>();
                updateWrapper.setSql("dz_quantity = dz_quantity + 1");
                updateWrapper.eq("id", articleGiveALike.getAssociatedId());
                articleService.update(updateWrapper);
            } else if(articleGiveALike.getType() == 2) {//文章评论
                UpdateWrapper<ArticleComments> updateWrapper = new UpdateWrapper<>();
                updateWrapper.setSql("dz_quantity = dz_quantity + 1");
                updateWrapper.eq("id", articleGiveALike.getAssociatedId());
                articleCommentsService.update(updateWrapper);
            }
            return R.ok("已点赞");
        } catch (DuplicateKeyException dke) {//使用数据库联合索引，避免并发产生重复数据
            return R.ok("已点赞");
        } catch (Exception e) {
            e.printStackTrace();
            //强制回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return R.failed();
        }
    }

}
