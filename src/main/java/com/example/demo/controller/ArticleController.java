package com.example.demo.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Article;
import com.example.demo.service.ArticleService;
import com.example.demo.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.*;

/**
 * 文章表
 *
 * @author fll
 * @since 2022-04-19
 */
@RestController
@RequestMapping("/article")
@Api(value = "article", tags = "文章表管理")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    //手动事务
    @Autowired
    private PlatformTransactionManager transactionManager;

    /**
     * 分页查询
     * @param page 分页对象
     * @param article 文章表
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getArticlePage(Page page, Article article) {
        return R.ok(articleService.page(page, Wrappers.query(article)));
    }

    /**
     * 通过id查询文章表
     * @param id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Integer id){
        return R.ok(articleService.getById(id));
    }

    /**
     * 新增文章表
     * @param article 文章表
     * @return R
     */
    @ApiOperation(value = "新增文章表", notes = "新增文章表")
    @PostMapping
    public R save(@RequestBody Article article){
        //手动开启事务
        DefaultTransactionDefinition dtd = new DefaultTransactionDefinition();
        dtd.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);//新发起一个事务
        TransactionStatus status = transactionManager.getTransaction(dtd);

        try {
            articleService.save(article);
            //手动提交事务
            transactionManager.commit(status);
            return R.ok();
        } catch (Exception e) {
            e.printStackTrace();
            //手动回滚事务
            transactionManager.rollback(status);
            return R.failed();
        }

    }

    /**
     * 修改文章表
     * @param article 文章表
     * @return R
     */
    @ApiOperation(value = "修改文章表", notes = "修改文章表")
    @PutMapping
    public R updateById(@RequestBody Article article){
        return R.ok(articleService.updateById(article));
    }

    /**
     * 通过id删除文章表
     * @param id
     * @return R
     */
    @ApiOperation(value = "通过id删除文章表", notes = "通过id删除文章表")
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Integer id){
        return R.ok(articleService.removeById(id));
    }

}
