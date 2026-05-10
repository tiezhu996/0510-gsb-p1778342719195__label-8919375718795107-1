package com.fruitshop.common;

public enum ResultCode {
    SUCCESS(200, "success"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未登录或登录已过期"),
    FORBIDDEN(403, "没有权限"),
    NOT_FOUND(404, "资源不存在"),
    ERROR(500, "服务器错误"),

    // 用户相关错误码
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_PASSWORD_ERROR(1002, "密码错误"),
    USER_PHONE_EXISTS(1003, "手机号已注册"),
    USER_DISABLED(1004, "账号已被禁用"),
    USER_LOGIN_FAILED(1005, "登录失败次数过多，请稍后再试"),

    // 商品相关错误码
    FRUIT_NOT_FOUND(2001, "商品不存在"),
    FRUIT_OFF_SHELF(2002, "商品已下架"),
    FRUIT_STOCK_NOT_ENOUGH(2003, "库存不足"),

    // 购物车相关错误码
    CART_ITEM_NOT_FOUND(3001, "购物车商品不存在"),
    CART_ITEM_LIMIT(3002, "购物车商品数量已达上限"),

    // 订单相关错误码
    ORDER_NOT_FOUND(4001, "订单不存在"),
    ORDER_STATUS_ERROR(4002, "订单状态错误"),
    ORDER_CREATE_FAILED(4003, "创建订单失败"),

    // 地址相关错误码
    ADDRESS_NOT_FOUND(5001, "地址不存在"),
    ADDRESS_LIMIT(5002, "地址数量已达上限"),

    // 评价相关错误码
    REVIEW_ORDER_NOT_COMPLETED(6001, "只能评价已完成的订单"),
    REVIEW_ITEM_NOT_IN_ORDER(6002, "该商品不在此订单中"),
    REVIEW_ALREADY_EXISTS(6003, "该商品已评价");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
