package com.fruitshop.service.impl;

import com.fruitshop.common.ResultCode;
import com.fruitshop.dto.request.ReviewCreateRequest;
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

import java.time.LocalDateTime;
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
    public ReviewVO createReview(Long userId, Long orderId, ReviewCreateRequest request) {
        Order order = orderMapper.findByIdAndUserId(orderId, userId);
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_FOUND);
        }
        if (order.getStatus() != Order.STATUS_COMPLETED) {
            throw new BusinessException("订单未完成，不能评价");
        }

        OrderItem orderItem = orderItemMapper.findById(request.getOrderItemId());
        if (orderItem == null) {
            throw new BusinessException("订单项不存在");
        }
        if (!orderItem.getOrderId().equals(orderId)) {
            throw new BusinessException("订单项不属于该订单");
        }

        Review existingReview = reviewMapper.findByOrderItemId(request.getOrderItemId());
        if (existingReview != null) {
            throw new BusinessException("该商品已评价，不能重复评价");
        }

        Review review = new Review();
        review.setOrderId(orderId);
        review.setOrderItemId(request.getOrderItemId());
        review.setUserId(userId);
        review.setFruitId(orderItem.getFruitId());
        review.setRating(request.getRating());
        review.setContent(request.getContent());
        review.setCreateTime(LocalDateTime.now());
        review.setUpdateTime(LocalDateTime.now());

        reviewMapper.insert(review);

        return ReviewVO.fromReview(review);
    }

    @Override
    public List<ReviewVO> getReviewsByOrderId(Long orderId, Long userId) {
        Order order = orderMapper.findByIdAndUserId(orderId, userId);
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_FOUND);
        }

        List<Review> reviews = reviewMapper.findByOrderId(orderId);
        return reviews.stream().map(ReviewVO::fromReview).collect(Collectors.toList());
    }

    @Override
    public boolean hasReviewed(Long orderItemId) {
        return reviewMapper.findByOrderItemId(orderItemId) != null;
    }
}
