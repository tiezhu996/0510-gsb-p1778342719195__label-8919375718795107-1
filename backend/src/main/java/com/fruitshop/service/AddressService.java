package com.fruitshop.service;

import com.fruitshop.dto.request.AddressRequest;
import com.fruitshop.vo.AddressVO;

import java.util.List;

public interface AddressService {

    List<AddressVO> getAddressList(Long userId);

    AddressVO getById(Long id, Long userId);

    Long addAddress(Long userId, AddressRequest request);

    void updateAddress(Long id, Long userId, AddressRequest request);

    void deleteAddress(Long id, Long userId);

    void setDefault(Long id, Long userId);
}
