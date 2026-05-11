package com.fruitshop.mapper;

import com.fruitshop.entity.Address;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AddressMapper {

    List<Address> findByUserId(@Param("userId") Long userId);

    Address findById(@Param("id") Long id);

    Address findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    Address findDefaultByUserId(@Param("userId") Long userId);

    int insert(Address address);

    int update(Address address);

    int delete(@Param("id") Long id);

    int resetDefault(@Param("userId") Long userId);

    int setDefault(@Param("id") Long id);

    int countByUserId(@Param("userId") Long userId);
}
