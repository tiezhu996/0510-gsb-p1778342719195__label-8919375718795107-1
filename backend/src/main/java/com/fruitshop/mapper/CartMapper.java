package com.fruitshop.mapper;

import com.fruitshop.entity.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartMapper {

    List<Cart> findByUserId(@Param("userId") Long userId);

    Cart findById(@Param("id") Long id);

    Cart findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    Cart findByUserFruitSpec(@Param("userId") Long userId,
                              @Param("fruitId") Long fruitId,
                              @Param("specId") Long specId);

    List<Cart> findByIds(@Param("ids") List<Long> ids, @Param("userId") Long userId);

    int insert(Cart cart);

    int updateQuantity(@Param("id") Long id, @Param("quantity") int quantity);

    int updateSelected(@Param("id") Long id, @Param("selected") int selected);

    int updateAllSelected(@Param("userId") Long userId, @Param("selected") int selected);

    int delete(@Param("id") Long id);

    int deleteByIds(@Param("ids") List<Long> ids);

    int countByUserId(@Param("userId") Long userId);
}
