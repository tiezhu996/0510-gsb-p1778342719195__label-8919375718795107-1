package com.fruitshop.mapper;

import com.fruitshop.entity.OrderReview;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderReviewMapper {

    OrderReview findByOrderItemId(@Param("orderItemId") Long orderItemId);

    List<OrderReview> findByOrderId(@Param("orderId") Long orderId);

    int insert(OrderReview review);

    int countByOrderId(@Param("orderId") Long orderId);
}
