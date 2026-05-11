package com.fruitshop.controller;

import com.fruitshop.common.Result;
import com.fruitshop.dto.request.LoginRequest;
import com.fruitshop.dto.request.PasswordUpdateRequest;
import com.fruitshop.dto.request.RegisterRequest;
import com.fruitshop.service.UserService;
import com.fruitshop.util.RequestContext;
import com.fruitshop.vo.UserVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Result<Map<String, Object>> register(@Valid @RequestBody RegisterRequest request) {
        Map<String, Object> data = userService.register(request);
        return Result.success(data);
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        Map<String, Object> data = userService.login(request);
        return Result.success(data);
    }

    @GetMapping("/info")
    public Result<UserVO> getUserInfo() {
        Long userId = RequestContext.getUserId();
        UserVO userVO = userService.getUserInfo(userId);
        return Result.success(userVO);
    }

    @PutMapping("/password")
    public Result<Void> updatePassword(@Valid @RequestBody PasswordUpdateRequest request) {
        Long userId = RequestContext.getUserId();
        userService.updatePassword(userId, request);
        return Result.success();
    }

    @PutMapping("/nickname")
    public Result<Void> updateNickname(@RequestBody Map<String, String> params) {
        Long userId = RequestContext.getUserId();
        String nickname = params.get("nickname");
        userService.updateNickname(userId, nickname);
        return Result.success();
    }
}
