package com.fruitshop.mapper;

import com.fruitshop.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {

    Order findById(@Param("id") Long id);

    Order findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    List<Order> findByUserId(@Param("userId") Long userId,
                              @Param("status") Integer status,
                              @Param("offset") int offset,
                              @Param("limit") int limit);

    long countByUserId(@Param("userId") Long userId, @Param("status") Integer status);

    int countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") int status);

    int insert(Order order);

    int updateStatus(@Param("id") Long id, @Param("status") int status);

    int pay(@Param("id") Long id);

    int ship(@Param("id") Long id);

    int shipWithLogistics(@Param("id") Long id, @Param("logisticsCompany") String logisticsCompany, @Param("logisticsNo") String logisticsNo);

    int receive(@Param("id") Long id);

    int cancel(@Param("id") Long id);

    int delete(@Param("id") Long id);
}
