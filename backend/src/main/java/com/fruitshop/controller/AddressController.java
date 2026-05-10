package com.fruitshop.controller;

import com.fruitshop.common.Result;
import com.fruitshop.dto.request.AddressRequest;
import com.fruitshop.service.AddressService;
import com.fruitshop.util.RequestContext;
import com.fruitshop.vo.AddressVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/list")
    public Result<List<AddressVO>> list() {
        Long userId = RequestContext.getUserId();
        List<AddressVO> addresses = addressService.getAddressList(userId);
        return Result.success(addresses);
    }

    @GetMapping("/{id}")
    public Result<AddressVO> detail(@PathVariable Long id) {
        Long userId = RequestContext.getUserId();
        AddressVO addressVO = addressService.getById(id, userId);
        return Result.success(addressVO);
    }

    @PostMapping
    public Result<Long> add(@Valid @RequestBody AddressRequest request) {
        Long userId = RequestContext.getUserId();
        Long addressId = addressService.addAddress(userId, request);
        return Result.success(addressId);
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody AddressRequest request) {
        Long userId = RequestContext.getUserId();
        addressService.updateAddress(id, userId, request);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        Long userId = RequestContext.getUserId();
        addressService.deleteAddress(id, userId);
        return Result.success();
    }

    @PutMapping("/{id}/default")
    public Result<Void> setDefault(@PathVariable Long id) {
        Long userId = RequestContext.getUserId();
        addressService.setDefault(id, userId);
        return Result.success();
    }
}
