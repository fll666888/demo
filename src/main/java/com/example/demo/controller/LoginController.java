package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.Users;
import com.example.demo.security.JwtAuthenticatioToken;
import com.example.demo.service.UsersService;
import com.example.demo.utils.SecurityUtil;
import com.example.demo.vo.LoginBean;
import com.example.demo.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
@Api(value = "login", tags = "登录管理")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsersService usersService;

    /**
     * 登录
     * @param loginBean
     * @param request
     * @return
     */
    @ApiOperation(value = "登录", notes = "登录")
    @PostMapping(value = "/login")
    public R login(@RequestBody LoginBean loginBean, HttpServletRequest request) {
        String username = loginBean.getUsername();
        String password = loginBean.getPassword();

        // 系统登录认证
        JwtAuthenticatioToken jwtAuthenticatioToken = SecurityUtil.login(request, username, password, authenticationManager);

        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(jwtAuthenticatioToken, SerializerFeature.WriteMapNullValue));

        jsonObject.put("username", username);
        return R.ok(jsonObject);
    }

    /**
     * 注册
     * @param loginBean
     * @return
     */
    @ApiOperation(value = "注册", notes = "注册")
    @PostMapping("/register")
    public R register(@RequestBody LoginBean loginBean) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", loginBean.getUsername());
        queryWrapper.eq("del", 0);
        Users users = usersService.getOne(queryWrapper);
        if(users == null) {
            Users u = new Users(loginBean.getUsername(), new BCryptPasswordEncoder().encode(loginBean.getPassword()));
            u.setCreatedate(LocalDateTime.now());
            return R.ok(usersService.save(u), "注册成功");
        }
        return R.failed("用户名已被注册");
    }

}
