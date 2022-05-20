package com.example.demo.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Users;
import com.example.demo.service.UsersService;
import com.example.demo.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 用户表
 *
 * @author fll
 * @since 2020-12-29
 */
@RestController
@RequestMapping("/users")
@Api(value = "users", tags = "用户表管理")
public class UsersController {

    @Autowired
    private UsersService usersService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param users 用户表
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getUsersPage(Page page, Users users) {
        return R.ok(usersService.page(page,Wrappers.query(users)));
    }

    /**
     * 通过id查询用户表
     * @param id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Integer id){
        return R.ok(usersService.getById(id));
    }

    /**
     * 新增用户表
     * @param users 用户表
     * @return R
     */
    @ApiOperation(value = "新增用户表", notes = "新增用户表")
    @PostMapping
    public R save(@RequestBody Users users){
        users.setCreatedate(LocalDateTime.now());
        return R.ok(usersService.save(users));
    }

    /**
     * 修改用户表
     * @param users 用户表
     * @return R
     */
    @ApiOperation(value = "修改用户表", notes = "修改用户表")
    @PutMapping
    public R updateById(@RequestBody Users users){
        users.setModifydate(LocalDateTime.now());
        return R.ok(usersService.updateById(users));
    }

    /**
     * 通过id删除用户表
     * @param id
     * @return R
     */
    @ApiOperation(value = "通过id删除用户表", notes = "通过id删除用户表")
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Integer id){
        return R.ok(usersService.removeById(id));
    }

}
