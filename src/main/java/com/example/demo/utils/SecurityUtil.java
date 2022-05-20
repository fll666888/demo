package com.example.demo.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import com.example.demo.security.JwtAuthenticatioToken;

/**
 * Security相关操作
 */
public class SecurityUtil {

    /**
     * 系统登录认证
     * @param request
     * @param username
     * @param password
     * @param authenticationManager
     * @return
     */
    public static JwtAuthenticatioToken login(HttpServletRequest request, String username, String password, AuthenticationManager authenticationManager) {
        JwtAuthenticatioToken authRequest = new JwtAuthenticatioToken(username, password);
        authRequest.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        // 执行登录认证过程
        Authentication authentication = authenticationManager.authenticate(authRequest);
        // 认证成功存储认证信息到上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 生成令牌并返回给客户端
        JwtAuthenticatioToken token = new JwtAuthenticatioToken(null, null, JwtTokenUtil.generateToken(authentication));
        return token;
    }

    /**
     * 获取令牌进行认证
     * @param request
     */
    public static void checkAuthentication(HttpServletRequest request) {
        // 获取令牌并根据令牌获取登录认证信息
        Authentication authentication = JwtTokenUtil.getAuthenticationeFromToken(request);
        // 设置登录认证信息到上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * 获取当前用户名
     * @return
     */
    public static String getUsername() {
        Authentication authentication = getAuthentication();
        return getUsername(authentication);
    }

    /**
     * 获取用户名
     * @return
     */
    public static String getUsername(Authentication authentication) {
        String username = null;
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal == null) {
                username = null;
            } else if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else if (principal instanceof String) {
                username = (String) principal;
            }

        }
        return username;
    }

    /**
     * 获取当前登录信息
     * @return
     */
    public static Authentication getAuthentication() {
        if(SecurityContextHolder.getContext() == null) {
            return null;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }

}
