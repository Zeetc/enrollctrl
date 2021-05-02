package com.catchiz.enrollctrl.handler;

import com.catchiz.enrollctrl.pojo.CommonResult;
import com.catchiz.enrollctrl.pojo.CommonStatus;
import com.catchiz.enrollctrl.utils.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component("authenticationSuccessHandler")
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        logger.info("登录成功");
        response.setContentType("application/json;charset=UTF-8");
        String username=authentication.getName();
        Map<String,Object> claims = new HashMap<>();
        claims.put("username",username);
        claims.put("authorities",authentication.getAuthorities());
        String token = JwtTokenUtil.generateToken(claims);
        response.getWriter().write(objectMapper.writeValueAsString(new CommonResult(CommonStatus.OK,"登录成功",token)));
    }
}