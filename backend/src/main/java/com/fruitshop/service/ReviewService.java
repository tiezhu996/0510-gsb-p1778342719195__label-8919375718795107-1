package com.fruitshop.service;

import com.fruitshop.dto.request.ReviewCreateRequest;
import com.fruitshop.vo.ReviewVO;

import java.util.List;

public interface ReviewService {

    ReviewVO createReview(Long userId, Long orderId, ReviewCreateRequest request);

    List<ReviewVO> getReviewsByOrderId(Long orderId, Long userId);

    boolean hasReviewed(Long orderItemId);
}
