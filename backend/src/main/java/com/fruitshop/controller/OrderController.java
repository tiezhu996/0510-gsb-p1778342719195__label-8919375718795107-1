package com.fruitshop.controller;

import com.fruitshop.common.PageResult;
import com.fruitshop.common.Result;
import com.fruitshop.dto.request.OrderCreateRequest;
import com.fruitshop.dto.request.OrderReviewRequest;
import com.fruitshop.entity.OrderReview;
import com.fruitshop.service.OrderService;
import com.fruitshop.util.RequestContext;
import com.fruitshop.vo.OrderVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
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

    @PostMapping("/{id}/review")
    public Result<Void> submitReview(@PathVariable Long id, @Valid @RequestBody OrderReviewRequest request) {
        Long userId = RequestContext.getUserId();
        orderService.submitReview(id, userId, request);
        return Result.success();
    }

    @GetMapping("/{id}/reviews")
    public Result<List<OrderReview>> getReviews(@PathVariable Long id) {
        Long userId = RequestContext.getUserId();
        List<OrderReview> reviews = orderService.getOrderReviews(id, userId);
        return Result.success(reviews);
    }
}
