package com.fruitshop.mapper;

import com.fruitshop.entity.Fruit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FruitMapper {

    Fruit findById(@Param("id") Long id);

    List<Fruit> findByCondition(@Param("categoryId") Long categoryId,
                                 @Param("keyword") String keyword,
                                 @Param("sort") String sort,
                                 @Param("offset") int offset,
                                 @Param("limit") int limit);

    long countByCondition(@Param("categoryId") Long categoryId,
                          @Param("keyword") String keyword);

    List<Fruit> findHotFruits(@Param("limit") int limit);

    List<Fruit> findNewFruits(@Param("limit") int limit);

    int updateStock(@Param("id") Long id, @Param("quantity") int quantity);

    int updateSales(@Param("id") Long id, @Param("quantity") int quantity);
}
