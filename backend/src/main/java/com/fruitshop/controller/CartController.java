package com.fruitshop.controller;

import com.fruitshop.common.Result;
import com.fruitshop.dto.request.CartAddRequest;
import com.fruitshop.service.CartService;
import com.fruitshop.util.RequestContext;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/list")
    public Result<Map<String, Object>> list() {
        Long userId = RequestContext.getUserId();
        Map<String, Object> data = cartService.getCartList(userId);
        return Result.success(data);
    }

    @PostMapping("/add")
    public Result<Long> add(@Valid @RequestBody CartAddRequest request) {
        Long userId = RequestContext.getUserId();
        Long cartId = cartService.addToCart(userId, request);
        return Result.success(cartId);
    }

    @PutMapping("/{id}/quantity")
    public Result<Void> updateQuantity(@PathVariable Long id, @RequestBody Map<String, Integer> params) {
        Long userId = RequestContext.getUserId();
        Integer quantity = params.get("quantity");
        cartService.updateQuantity(id, userId, quantity);
        return Result.success();
    }

    @PutMapping("/{id}/selected")
    public Result<Void> updateSelected(@PathVariable Long id, @RequestBody Map<String, Boolean> params) {
        Long userId = RequestContext.getUserId();
        Boolean selected = params.get("selected");
        cartService.updateSelected(id, userId, selected);
        return Result.success();
    }

    @PutMapping("/selectAll")
    public Result<Void> selectAll(@RequestBody Map<String, Boolean> params) {
        Long userId = RequestContext.getUserId();
        Boolean selected = params.get("selected");
        cartService.updateAllSelected(userId, selected);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        Long userId = RequestContext.getUserId();
        cartService.deleteCartItem(id, userId);
        return Result.success();
    }
}
