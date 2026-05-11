package com.fruitshop.service;

import com.fruitshop.dto.request.ReviewRequest;
import com.fruitshop.vo.ReviewVO;

import java.util.List;

public interface ReviewService {

    void submitReview(Long userId, Long orderId, ReviewRequest request);

    List<ReviewVO> getOrderReviews(Long orderId, Long userId);

    boolean hasReviewed(Long orderId);
}
