package com.fruitshop.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fruitshop.common.Result;
import com.fruitshop.common.ResultCode;
import com.fruitshop.util.JwtUtil;
import com.fruitshop.util.RequestContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    @Lazy
    @Autowired
    private ObjectMapper objectMapper;

    public JwtInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // OPTIONS请求直接放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            writeUnauthorized(response);
            return false;
        }

        token = token.substring(7);
        Long userId = jwtUtil.getUserIdFromToken(token);
        if (userId == null) {
            writeUnauthorized(response);
            return false;
        }

        // 将用户ID存入ThreadLocal
        RequestContext.setUserId(userId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        RequestContext.clear();
    }

    private void writeUnauthorized(HttpServletResponse response) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        Result<?> result = Result.error(ResultCode.UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
