package com.fruitshop.controller;

import com.fruitshop.common.PageResult;
import com.fruitshop.common.Result;
import com.fruitshop.dto.request.OrderCreateRequest;
import com.fruitshop.dto.request.ReviewCreateRequest;
import com.fruitshop.service.OrderService;
import com.fruitshop.service.ReviewService;
import com.fruitshop.util.RequestContext;
import com.fruitshop.vo.OrderVO;
import com.fruitshop.vo.ReviewVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private final ReviewService reviewService;

    public OrderController(OrderService orderService, ReviewService reviewService) {
        this.orderService = orderService;
        this.reviewService = reviewService;
    }

    @PostMapping("/create")
    public Result<Map<String, Object>> create(@Valid @RequestBody OrderCreateRequest request) {
        Long userId = RequestContext.getUserId();
        Map<String, Object> data = orderService.createOrder(userId, request);
        return Result.success(data);
    }

    @GetMapping("/list")
    public Result<PageResult<OrderVO>> list(
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long userId = RequestContext.getUserId();
        PageResult<OrderVO> pageResult = orderService.getOrderList(userId, status, page, size);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    public Result<OrderVO> detail(@PathVariable Long id) {
        Long userId = RequestContext.getUserId();
        OrderVO orderVO = orderService.getOrderDetail(id, userId);
        return Result.success(orderVO);
    }

    @PostMapping("/{id}/pay")
    public Result<Void> pay(@PathVariable Long id) {
        Long userId = RequestContext.getUserId();
        orderService.payOrder(id, userId);
        return Result.success();
    }

    @PostMapping("/{id}/ship")
    public Result<Void> ship(@PathVariable Long id,
                             @RequestParam String logisticsCompany,
                             @RequestParam String logisticsNo) {
        orderService.shipOrder(id, logisticsCompany, logisticsNo);
        return Result.success();
    }

    @PostMapping("/{id}/cancel")
    public Result<Void> cancel(@PathVariable Long id) {
        Long userId = RequestContext.getUserId();
        orderService.cancelOrder(id, userId);
        return Result.success();
    }

    @PostMapping("/{id}/receive")
    public Result<Void> receive(@PathVariable Long id) {
        Long userId = RequestContext.getUserId();
        orderService.receiveOrder(id, userId);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        Long userId = RequestContext.getUserId();
        orderService.deleteOrder(id, userId);
        return Result.success();
    }

    @PostMapping("/{orderId}/review")
    public Result<ReviewVO> createReview(
            @PathVariable Long orderId,
            @Valid @RequestBody ReviewCreateRequest request) {
        Long userId = RequestContext.getUserId();
        ReviewVO reviewVO = reviewService.createReview(userId, orderId, request);
        return Result.success(reviewVO);
    }

    @GetMapping("/{orderId}/reviews")
    public Result<List<ReviewVO>> getReviews(@PathVariable Long orderId) {
        Long userId = RequestContext.getUserId();
        List<ReviewVO> reviews = reviewService.getReviewsByOrderId(orderId, userId);
        return Result.success(reviews);
    }
}
