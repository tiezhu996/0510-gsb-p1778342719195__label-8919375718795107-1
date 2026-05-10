package com.fruitshop.service.impl;

import com.fruitshop.common.ResultCode;
import com.fruitshop.dto.request.LoginRequest;
import com.fruitshop.dto.request.PasswordUpdateRequest;
import com.fruitshop.dto.request.RegisterRequest;
import com.fruitshop.entity.User;
import com.fruitshop.exception.BusinessException;
import com.fruitshop.mapper.OrderMapper;
import com.fruitshop.mapper.UserMapper;
import com.fruitshop.service.UserService;
import com.fruitshop.util.JwtUtil;
import com.fruitshop.util.PasswordUtil;
import com.fruitshop.vo.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final OrderMapper orderMapper;
    private final PasswordUtil passwordUtil;
    private final JwtUtil jwtUtil;

    public UserServiceImpl(UserMapper userMapper, OrderMapper orderMapper,
                           PasswordUtil passwordUtil, JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.orderMapper = orderMapper;
        this.passwordUtil = passwordUtil;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
    public Map<String, Object> register(RegisterRequest request) {
        // 验证两次密码是否一致
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException("两次输入的密码不一致");
        }

        // 检查手机号是否已注册
        User existUser = userMapper.findByPhone(request.getPhone());
        if (existUser != null) {
            throw new BusinessException(ResultCode.USER_PHONE_EXISTS);
        }

        // 创建用户
        User user = new User();
        user.setPhone(request.getPhone());
        user.setPassword(passwordUtil.encode(request.getPassword()));
        user.setNickname(request.getNickname());
        user.setStatus(1);
        userMapper.insert(user);

        // 生成Token
        String token = jwtUtil.generateToken(user.getId(), user.getPhone(), false);

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", UserVO.fromUser(user));
        return result;
    }

    @Override
    public Map<String, Object> login(LoginRequest request) {
        // 查找用户
        User user = userMapper.findByPhone(request.getPhone());
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // 验证密码
        if (!passwordUtil.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.USER_PASSWORD_ERROR);
        }

        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }

        // 生成Token
        boolean remember = request.getRemember() != null && request.getRemember();
        String token = jwtUtil.generateToken(user.getId(), user.getPhone(), remember);

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", UserVO.fromUser(user));
        return result;
    }

    @Override
    public UserVO getUserInfo(Long userId) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        UserVO userVO = UserVO.fromUser(user);

        // 获取订单统计
        UserVO.OrderCountVO orderCount = new UserVO.OrderCountVO();
        orderCount.setUnpaid(orderMapper.countByUserIdAndStatus(userId, 0));
        orderCount.setUnshipped(orderMapper.countByUserIdAndStatus(userId, 1));
        orderCount.setUnreceived(orderMapper.countByUserIdAndStatus(userId, 2));
        orderCount.setCompleted(orderMapper.countByUserIdAndStatus(userId, 3));
        userVO.setOrderCount(orderCount);

        return userVO;
    }

    @Override
    @Transactional
    public void updatePassword(Long userId, PasswordUpdateRequest request) {
        // 验证两次密码是否一致
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException("两次输入的密码不一致");
        }

        // 获取用户
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // 验证旧密码
        if (!passwordUtil.matches(request.getOldPassword(), user.getPassword())) {
            throw new BusinessException("旧密码错误");
        }

        // 更新密码
        userMapper.updatePassword(userId, passwordUtil.encode(request.getNewPassword()));
    }

    @Override
    @Transactional
    public void updateNickname(Long userId, String nickname) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        userMapper.updateNickname(userId, nickname);
    }
}
