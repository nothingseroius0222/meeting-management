package com.meeting.filter;

import com.meeting.entity.User;
import com.meeting.service.UserService;
import com.meeting.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //1.从请求头中获取JWT
        String authHeader = request.getHeader("Authorization");
        String userName = null;
        String token = null;

        //2.检查Header是否以“Bearer”开头
        if(authHeader != null && authHeader.startsWith("Bearer")) {
            token = authHeader.substring(7);
            try{
                userName = jwtUtil.getUsernameFromToken(token);
            }catch (Exception e){
                logger.warn("JWT解析失败：" + e.getMessage());
            }
        }
        //4.如果拿到了用户信息，且当前SecurityContext中没有认证信息
        if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
            //5.获取用户信息
            User user = userService.getUserByUsername(userName);
            //6.验证token是否过期
            if(jwtUtil.validateToken(token,userName)){
                //7.创建Authentication对象
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, null);
                //8.设置一些额外的请求详情，比如IP地址等
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //9.将认证对象放入SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        //10.无论是否认证成功，都继续执行过滤链
        filterChain.doFilter(request,response);
    }
}
