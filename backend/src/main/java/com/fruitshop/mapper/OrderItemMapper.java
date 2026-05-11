package com.fruitshop.mapper;

import com.fruitshop.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderItemMapper {

    OrderItem findById(@Param("id") Long id);

    List<OrderItem> findByOrderId(@Param("orderId") Long orderId);

    int insert(OrderItem item);

    int batchInsert(@Param("items") List<OrderItem> items);

    int deleteByOrderId(@Param("orderId") Long orderId);
}
