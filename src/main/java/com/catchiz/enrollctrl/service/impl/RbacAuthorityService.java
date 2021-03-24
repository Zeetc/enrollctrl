package com.catchiz.enrollctrl.service.impl;

import com.catchiz.enrollctrl.utils.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

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
        //if(!referer.contains("z3117538n4.wicp.vip")&&!referer.contains("311l7b5384.goho.co")&&!referer.contains("localhost"))
        log.info("current request is:" + request.getRequestURI());
        String token = request.getHeader("Authorization");
        if(token==null||redisTemplate.opsForValue().get(token)!=null)return false;
        Claims claims= JwtTokenUtil.getClaimsFromToken(token);
        if(claims==null||claims.getExpiration().before(new Date()))return false;
        String username=claims.getSubject();
        if(!StringUtils.hasText(username))return false;
        // 自定义登录规则，如果有userID就可以访问/file 和/user
        // 目前只拦截/manager接口
        // 自定义拦截规则之后security可以禁用session，否则禁用session后所有RULE规则都没法用
        String requestURI= request.getRequestURI();
        //TODO 鉴权
        if(requestURI.startsWith("/manager")){
            return (boolean) claims.get("isManager");
        }
        return true;
    }
}