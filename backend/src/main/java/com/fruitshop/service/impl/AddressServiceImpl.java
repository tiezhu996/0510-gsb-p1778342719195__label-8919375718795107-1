package com.fruitshop.service.impl;

import com.fruitshop.common.ResultCode;
import com.fruitshop.dto.request.AddressRequest;
import com.fruitshop.entity.Address;
import com.fruitshop.exception.BusinessException;
import com.fruitshop.mapper.AddressMapper;
import com.fruitshop.service.AddressService;
import com.fruitshop.vo.AddressVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {

    private static final int MAX_ADDRESS_COUNT = 20;

    private final AddressMapper addressMapper;

    public AddressServiceImpl(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }

    @Override
    public List<AddressVO> getAddressList(Long userId) {
        List<Address> addresses = addressMapper.findByUserId(userId);
        return addresses.stream()
                .map(AddressVO::fromAddress)
                .collect(Collectors.toList());
    }

    @Override
    public AddressVO getById(Long id, Long userId) {
        Address address = addressMapper.findByIdAndUserId(id, userId);
        if (address == null) {
            throw new BusinessException(ResultCode.ADDRESS_NOT_FOUND);
        }
        return AddressVO.fromAddress(address);
    }

    @Override
    @Transactional
    public Long addAddress(Long userId, AddressRequest request) {
        // 检查地址数量
        int count = addressMapper.countByUserId(userId);
        if (count >= MAX_ADDRESS_COUNT) {
            throw new BusinessException(ResultCode.ADDRESS_LIMIT);
        }

        Address address = new Address();
        address.setUserId(userId);
        address.setName(request.getName());
        address.setPhone(request.getPhone());
        address.setProvince(request.getProvince());
        address.setCity(request.getCity());
        address.setDistrict(request.getDistrict());
        address.setDetail(request.getDetail());

        // 如果是第一个地址或者设置为默认，则设为默认地址
        if (count == 0 || (request.getIsDefault() != null && request.getIsDefault())) {
            addressMapper.resetDefault(userId);
            address.setIsDefault(1);
        } else {
            address.setIsDefault(0);
        }

        addressMapper.insert(address);
        return address.getId();
    }

    @Override
    @Transactional
    public void updateAddress(Long id, Long userId, AddressRequest request) {
        Address address = addressMapper.findByIdAndUserId(id, userId);
        if (address == null) {
            throw new BusinessException(ResultCode.ADDRESS_NOT_FOUND);
        }

        address.setName(request.getName());
        address.setPhone(request.getPhone());
        address.setProvince(request.getProvince());
        address.setCity(request.getCity());
        address.setDistrict(request.getDistrict());
        address.setDetail(request.getDetail());

        if (request.getIsDefault() != null && request.getIsDefault()) {
            addressMapper.resetDefault(userId);
            address.setIsDefault(1);
        }

        addressMapper.update(address);
    }

    @Override
    @Transactional
    public void deleteAddress(Long id, Long userId) {
        Address address = addressMapper.findByIdAndUserId(id, userId);
        if (address == null) {
            throw new BusinessException(ResultCode.ADDRESS_NOT_FOUND);
        }

        addressMapper.delete(id);

        // 如果删除的是默认地址，将第一个地址设为默认
        if (address.getIsDefault() == 1) {
            List<Address> addresses = addressMapper.findByUserId(userId);
            if (!addresses.isEmpty()) {
                addressMapper.setDefault(addresses.get(0).getId());
            }
        }
    }

    @Override
    @Transactional
    public void setDefault(Long id, Long userId) {
        Address address = addressMapper.findByIdAndUserId(id, userId);
        if (address == null) {
            throw new BusinessException(ResultCode.ADDRESS_NOT_FOUND);
        }

        addressMapper.resetDefault(userId);
        addressMapper.setDefault(id);
    }
}
