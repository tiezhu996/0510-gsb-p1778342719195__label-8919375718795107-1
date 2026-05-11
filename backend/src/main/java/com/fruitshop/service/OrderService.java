package com.fruitshop.service;

import com.fruitshop.common.PageResult;
import com.fruitshop.dto.request.OrderCreateRequest;
import com.fruitshop.vo.OrderVO;

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
}
