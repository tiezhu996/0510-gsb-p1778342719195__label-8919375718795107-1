package com.fruitshop.mapper;

import com.fruitshop.entity.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReviewMapper {

    Review findById(@Param("id") Long id);

    Review findByOrderItemId(@Param("orderItemId") Long orderItemId);

    List<Review> findByOrderId(@Param("orderId") Long orderId);

    List<Review> findByUserId(@Param("userId") Long userId);

    int insert(Review review);

    int countByOrderId(@Param("orderId") Long orderId);
}
