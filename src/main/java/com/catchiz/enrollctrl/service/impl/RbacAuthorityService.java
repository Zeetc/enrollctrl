package com.catchiz.enrollctrl.service.impl;

import com.catchiz.enrollctrl.utils.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * RBAC 所有的请求都会到这里做权限校验
 */
@Slf4j
@Component("rbacService")
public class RbacAuthorityService {

    public RbacAuthorityService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Bean
    private AntPathMatcher antPathMatcher() {
        return new AntPathMatcher();
    }

    private final StringRedisTemplate redisTemplate;

    public boolean hasPermission(HttpServletRequest request) {
        if("gateway".equals(request.getHeader("from")))return true;
        log.info("current request is:----->" + request.getRequestURI());
        String token = request.getHeader("Authorization");
        if(token==null||redisTemplate.opsForValue().get(token)!=null)return false;
        Claims claims= JwtTokenUtil.getClaimsFromToken(token);
        if(claims==null||claims.getExpiration().before(new Date()))return false;
        String username= (String) claims.get("username");
        if(username==null||username.equals(""))return false;
        System.out.println(claims.get("authorities"));
        // 自定义拦截规则之后security可以禁用session，否则禁用session后所有RULE规则都没法用
        List<LinkedHashMap<String,String>> list = (List<LinkedHashMap<String, String>>) claims.get("authorities");
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        for (LinkedHashMap<String, String> map : list) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                authorityList.add(new SimpleGrantedAuthority(entry.getValue()));
            }
        }
        //再此构造authentication，放入spring security context中，主要是为了后续的方法头鉴权
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                username,
                null,
                authorityList
        );
        System.out.println(authorityList);
        SecurityContextHolder.getContext().setAuthentication(authRequest);
        return true;
    }
}