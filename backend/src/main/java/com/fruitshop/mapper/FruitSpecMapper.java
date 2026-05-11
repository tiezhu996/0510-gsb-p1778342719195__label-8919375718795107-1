package com.fruitshop.mapper;

import com.fruitshop.entity.FruitSpec;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FruitSpecMapper {

    List<FruitSpec> findByFruitId(@Param("fruitId") Long fruitId);

    FruitSpec findById(@Param("id") Long id);

    int updateStock(@Param("id") Long id, @Param("quantity") int quantity);
}
