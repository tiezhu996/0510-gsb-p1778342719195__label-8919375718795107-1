package com.fruitshop.mapper;

import com.fruitshop.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    User findById(@Param("id") Long id);

    User findByPhone(@Param("phone") String phone);

    int insert(User user);

    int update(User user);

    int updatePassword(@Param("id") Long id, @Param("password") String password);

    int updateNickname(@Param("id") Long id, @Param("nickname") String nickname);
}
