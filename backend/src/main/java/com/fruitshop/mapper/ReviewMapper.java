package com.fruitshop.mapper;

import com.fruitshop.entity.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReviewMapper {

    int insert(Review review);

    Review findByOrderItemId(@Param("orderItemId") Long orderItemId);

    List<Review> findByOrderId(@Param("orderId") Long orderId);

    int countByOrderId(@Param("orderId") Long orderId);

    boolean existsByOrderId(@Param("orderId") Long orderId);

    List<Long> findReviewedItemIdsByOrderId(@Param("orderId") Long orderId);
}
