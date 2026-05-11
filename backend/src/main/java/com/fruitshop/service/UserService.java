package com.fruitshop.service;

import com.fruitshop.dto.request.LoginRequest;
import com.fruitshop.dto.request.PasswordUpdateRequest;
import com.fruitshop.dto.request.RegisterRequest;
import com.fruitshop.vo.UserVO;

import java.util.Map;

public interface UserService {

    Map<String, Object> register(RegisterRequest request);

    Map<String, Object> login(LoginRequest request);

    UserVO getUserInfo(Long userId);

    void updatePassword(Long userId, PasswordUpdateRequest request);

    void updateNickname(Long userId, String nickname);
}
