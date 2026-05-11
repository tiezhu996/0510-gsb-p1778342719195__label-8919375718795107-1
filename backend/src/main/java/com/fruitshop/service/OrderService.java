package com.fruitshop.service;

import com.fruitshop.common.PageResult;
import com.fruitshop.dto.request.OrderCreateRequest;
import com.fruitshop.dto.request.OrderReviewRequest;
import com.fruitshop.entity.OrderReview;
import com.fruitshop.vo.OrderVO;

import java.util.List;
import java.util.Map;

public interface OrderService {

    Map<String, Object> createOrder(Long userId, OrderCreateRequest request);

    PageResult<OrderVO> getOrderList(Long userId, Integer status, int page, int size);

    OrderVO getOrderDetail(Long id, Long userId);

    void payOrder(Long id, Long userId);

    void shipOrder(Long id, String logisticsCompany, String logisticsNo);

    void cancelOrder(Long id, Long userId);

    void receiveOrder(Long id, Long userId);

    void deleteOrder(Long id, Long userId);

    void submitReview(Long orderId, Long userId, OrderReviewRequest request);

    List<OrderReview> getOrderReviews(Long orderId, Long userId);
}
