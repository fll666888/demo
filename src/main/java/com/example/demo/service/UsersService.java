package com.example.demo.service;

import com.example.demo.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author fll
 * @since 2020-12-29
 */
public interface UsersService extends IService<Users> {

    /**
     * 根据用户名查找用户
     * @param username
     * @return
     */
    Users findByUsername(String username);

    /**
     * 查找用户的菜单权限标识集合
     * @param username
     * @return
     */
    Set<String> findPermissions(String username);

}
