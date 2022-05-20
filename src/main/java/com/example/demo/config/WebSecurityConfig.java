package com.example.demo.config;

import com.example.demo.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //全局
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UserDetailsService userDetailsService;

    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        // 使用自定义登录身份认证组件
        auth.authenticationProvider(new JwtAuthenticationProvider(userDetailsService));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                // 禁用 csrf, 由于使用的是JWT，我们这里不需要csrf
                .and().csrf().disable()
                .authorizeRequests()
                // 跨域预检请求
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 登录URL
                .antMatchers("/login").permitAll()
                // 注册URL
                .antMatchers("/register").permitAll()
                // swagger
                .antMatchers("/swagger**/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/v2/**").permitAll()
                // druid
                .antMatchers("/druid/**").permitAll()
                // 其他所有请求需要身份认证
                .anyRequest().authenticated();
        // 开启登录认证流程过滤器 ，如果使用LoginController的login接口, 需要注释掉此过滤器，根据使用习惯二选一即可
        //http.addFilterBefore(new JwtLoginFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
        // 访问控制时登录状态检查过滤器
        http.addFilterBefore(new JwtAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
        // 退出登录处理器
        //http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
        http.logout().addLogoutHandler(new CustomLogoutHandler()).logoutSuccessHandler(new CustomLogoutSuccessHandler());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

}
