package com.fruitshop.service.impl;

import com.fruitshop.common.ResultCode;
import com.fruitshop.dto.request.ReviewRequest;
import com.fruitshop.entity.Order;
import com.fruitshop.entity.OrderItem;
import com.fruitshop.entity.Review;
import com.fruitshop.exception.BusinessException;
import com.fruitshop.mapper.OrderItemMapper;
import com.fruitshop.mapper.OrderMapper;
import com.fruitshop.mapper.ReviewMapper;
import com.fruitshop.service.ReviewService;
import com.fruitshop.vo.ReviewVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewMapper reviewMapper;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    public ReviewServiceImpl(ReviewMapper reviewMapper, OrderMapper orderMapper, OrderItemMapper orderItemMapper) {
        this.reviewMapper = reviewMapper;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
    }

    @Override
    @Transactional
    public void submitReview(Long userId, Long orderId, ReviewRequest request) {
        Order order = orderMapper.findByIdAndUserId(orderId, userId);
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_FOUND);
        }
        if (order.getStatus() != Order.STATUS_COMPLETED) {
            throw new BusinessException(ResultCode.REVIEW_ORDER_NOT_COMPLETED);
        }

        OrderItem orderItem = orderItemMapper.findByOrderId(orderId).stream()
                .filter(item -> item.getId().equals(request.getOrderItemId()))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ResultCode.REVIEW_ITEM_NOT_IN_ORDER));

        Review existing = reviewMapper.findByOrderItemId(request.getOrderItemId());
        if (existing != null) {
            throw new BusinessException(ResultCode.REVIEW_ALREADY_EXISTS);
        }

        Review review = new Review();
        review.setOrderId(orderId);
        review.setUserId(userId);
        review.setFruitId(orderItem.getFruitId());
        review.setOrderItemId(request.getOrderItemId());
        review.setRating(request.getRating());
        review.setContent(request.getContent());
        reviewMapper.insert(review);
    }

    @Override
    public List<ReviewVO> getOrderReviews(Long orderId, Long userId) {
        Order order = orderMapper.findByIdAndUserId(orderId, userId);
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_FOUND);
        }
        List<Review> reviews = reviewMapper.findByOrderId(orderId);
        return reviews.stream().map(ReviewVO::fromReview).collect(Collectors.toList());
    }

    @Override
    public boolean hasReviewed(Long orderId) {
        return reviewMapper.existsByOrderId(orderId);
    }
}
