package com.fruitshop.service;

import com.fruitshop.dto.request.CartAddRequest;
import com.fruitshop.vo.CartVO;

import java.util.List;
import java.util.Map;

public interface CartService {

    Map<String, Object> getCartList(Long userId);

    Long addToCart(Long userId, CartAddRequest request);

    void updateQuantity(Long id, Long userId, int quantity);

    void updateSelected(Long id, Long userId, boolean selected);

    void updateAllSelected(Long userId, boolean selected);

    void deleteCartItem(Long id, Long userId);

    List<CartVO> getCartItemsByIds(List<Long> ids, Long userId);
}
