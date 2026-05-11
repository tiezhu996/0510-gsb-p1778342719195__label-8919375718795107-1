package com.fruitshop.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

public class RequestContext {

    private static final String USER_ID_ATTR = "currentUserId";

    public static void setCurrentUserId(Long userId) {
        HttpServletRequest request = getCurrentRequest();
        if (request != null) {
            request.setAttribute(USER_ID_ATTR, userId);
        }
    }

    public static Long getCurrentUserId() {
        HttpServletRequest request = getCurrentRequest();
        if (request != null) {
            Object userId = request.getAttribute(USER_ID_ATTR);
            if (userId instanceof Long) {
                return (Long) userId;
            }
        }
        return null;
    }

    public static Long getUserId() {
        return getCurrentUserId();
    }

    public static void setUserId(Long userId) {
        setCurrentUserId(userId);
    }

    public static void clear() {
        HttpServletRequest request = getCurrentRequest();
        if (request != null) {
            request.removeAttribute(USER_ID_ATTR);
        }
    }

    private static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs != null ? attrs.getRequest() : null;
    }
}
