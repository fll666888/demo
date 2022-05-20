package com.example.demo.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Goods;
import com.example.demo.service.GoodsService;
import com.example.demo.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * 商品表
 *
 * @author fll
 * @since 2022-03-28
 */
@RestController
@RequestMapping("/goods")
@Api(value = "goods", tags = "商品表管理")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param goods 商品表
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getGoodsPage(Page page, Goods goods) {
        return R.ok(goodsService.page(page,Wrappers.query(goods)));
    }

    /**
     * 通过id查询商品表
     * @param id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Integer id){
        return R.ok(goodsService.getById(id));
    }

    /**
     * 新增商品表
     * @param goods 商品表
     * @return R
     */
    @ApiOperation(value = "新增商品表", notes = "新增商品表")
    @PostMapping
    public R save(@RequestBody Goods goods){
        return R.ok(goodsService.save(goods));
    }

    /**
     * 修改商品表
     * @param goods 商品表
     * @return R
     */
    @ApiOperation(value = "修改商品表", notes = "修改商品表")
    @PutMapping
    public R updateById(@RequestBody Goods goods){
        return R.ok(goodsService.updateById(goods));
    }

    /**
     * 通过id删除商品表
     * @param id
     * @return R
     */
    @ApiOperation(value = "通过id删除商品表", notes = "通过id删除商品表")
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Integer id){
        return R.ok(goodsService.removeById(id));
    }

}
