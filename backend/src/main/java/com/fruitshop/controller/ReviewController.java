package com.fruitshop.controller;

import com.fruitshop.common.Result;
import com.fruitshop.dto.request.ReviewRequest;
import com.fruitshop.service.ReviewService;
import com.fruitshop.util.RequestContext;
import com.fruitshop.vo.ReviewVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/order/{orderId}")
    public Result<Void> submitReview(@PathVariable Long orderId,
                                     @Valid @RequestBody ReviewRequest request) {
        Long userId = RequestContext.getUserId();
        reviewService.submitReview(userId, orderId, request);
        return Result.success();
    }

    @GetMapping("/order/{orderId}")
    public Result<List<ReviewVO>> getOrderReviews(@PathVariable Long orderId) {
        Long userId = RequestContext.getUserId();
        List<ReviewVO> reviews = reviewService.getOrderReviews(orderId, userId);
        return Result.success(reviews);
    }
}
