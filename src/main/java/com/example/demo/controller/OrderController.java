package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Goods;
import com.example.demo.entity.Order;
import com.example.demo.redis.DistributedLock;
import com.example.demo.service.GoodsService;
import com.example.demo.service.OrderService;
import com.example.demo.utils.MakeOrderNum;
import com.example.demo.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 订单表
 *
 * @author fll
 * @since 2022-03-28
 */
@RestController
@RequestMapping("/order")
@Api(value = "order", tags = "订单表管理")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private DistributedLock distributedLock;

    /**
     * 分页查询
     * @param page 分页对象
     * @param order 订单表
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getOrderPage(Page page, Order order) {
        return R.ok(orderService.page(page, Wrappers.query(order)));
    }

    /**
     * 通过id查询订单表
     * @param id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Integer id){
        return R.ok(orderService.getById(id));
    }

    /**
     * 新增订单表
     * @param order 订单表
     * @return R
     */
    @ApiOperation(value = "新增订单表", notes = "新增订单表")
    @PostMapping
    public R save(@RequestBody Order order){
        long currentTime = System.currentTimeMillis();
        long expireTime = 3000L;
        String valueStr = String.valueOf(currentTime + expireTime);
        boolean lock = distributedLock.lock("goodsId::" + order.getGoodsId(), valueStr);
        if(lock) {
            //1.查库存
            Long inventory  = goodsService.getById(order.getGoodsId()).getInventory();
            if(inventory.intValue() == 0) {
                return R.failed("库存为空！！！");
            }
            if(inventory.intValue() < order.getGoodsNumber()) {
                return R.failed("库存不够！！！");
            }
            order.setOrderNumber(MakeOrderNum.makeOrderNum());
            order.setCreatedate(LocalDateTime.now());
            orderService.save(order);
            UpdateWrapper<Goods> updateWrapper = new UpdateWrapper<>();
            updateWrapper.setSql("inventory = inventory -" + order.getGoodsNumber());
            updateWrapper.set("modifyDate", LocalDateTime.now());
            updateWrapper.eq("id", order.getGoodsId());
            goodsService.update(updateWrapper);
            distributedLock.unlock("goodsId::" + order.getGoodsId(), valueStr);
            return R.ok("下单成功！！！");
        } else {
            return R.failed("系统繁忙，请重试下单！！！");
        }
    }

    /**
     * 修改订单表
     * @param order 订单表
     * @return R
     */
    @ApiOperation(value = "修改订单表", notes = "修改订单表")
    @PutMapping
    public R updateById(@RequestBody Order order){
        return R.ok(orderService.updateById(order));
    }

    /**
     * 通过id删除订单表
     * @param id
     * @return R
     */
    @ApiOperation(value = "通过id删除订单表", notes = "通过id删除订单表")
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Integer id){
        return R.ok(orderService.removeById(id));
    }

}
