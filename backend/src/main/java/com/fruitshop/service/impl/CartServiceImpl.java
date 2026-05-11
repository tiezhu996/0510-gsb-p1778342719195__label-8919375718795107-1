package com.fruitshop.service.impl;

import com.fruitshop.common.ResultCode;
import com.fruitshop.dto.request.CartAddRequest;
import com.fruitshop.entity.Cart;
import com.fruitshop.entity.Fruit;
import com.fruitshop.entity.FruitSpec;
import com.fruitshop.exception.BusinessException;
import com.fruitshop.mapper.CartMapper;
import com.fruitshop.mapper.FruitMapper;
import com.fruitshop.mapper.FruitSpecMapper;
import com.fruitshop.service.CartService;
import com.fruitshop.vo.CartVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private static final int MAX_CART_COUNT = 50;
    private static final int MAX_ITEM_QUANTITY = 99;

    private final CartMapper cartMapper;
    private final FruitMapper fruitMapper;
    private final FruitSpecMapper fruitSpecMapper;

    public CartServiceImpl(CartMapper cartMapper, FruitMapper fruitMapper, FruitSpecMapper fruitSpecMapper) {
        this.cartMapper = cartMapper;
        this.fruitMapper = fruitMapper;
        this.fruitSpecMapper = fruitSpecMapper;
    }

    @Override
    public Map<String, Object> getCartList(Long userId) {
        List<Cart> carts = cartMapper.findByUserId(userId);

        List<CartVO> items = carts.stream()
                .map(CartVO::fromCart)
                .collect(Collectors.toList());

        int totalCount = 0;
        int selectedCount = 0;
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (CartVO item : items) {
            totalCount += item.getQuantity();
            if (item.getSelected()) {
                selectedCount += item.getQuantity();
                totalPrice = totalPrice.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("items", items);
        result.put("totalCount", totalCount);
        result.put("selectedCount", selectedCount);
        result.put("totalPrice", totalPrice);
        return result;
    }

    @Override
    @Transactional
    public Long addToCart(Long userId, CartAddRequest request) {
        // 检查商品是否存在
        Fruit fruit = fruitMapper.findById(request.getFruitId());
        if (fruit == null) {
            throw new BusinessException(ResultCode.FRUIT_NOT_FOUND);
        }
        if (fruit.getStatus() == 0) {
            throw new BusinessException(ResultCode.FRUIT_OFF_SHELF);
        }

        // 如果有规格，检查规格
        if (request.getSpecId() != null) {
            FruitSpec spec = fruitSpecMapper.findById(request.getSpecId());
            if (spec == null || !spec.getFruitId().equals(request.getFruitId())) {
                throw new BusinessException("商品规格不存在");
            }
        }

        // 检查是否已在购物车中
        Cart existCart = cartMapper.findByUserFruitSpec(userId, request.getFruitId(), request.getSpecId());
        if (existCart != null) {
            // 已存在，增加数量
            int newQuantity = existCart.getQuantity() + request.getQuantity();
            if (newQuantity > MAX_ITEM_QUANTITY) {
                newQuantity = MAX_ITEM_QUANTITY;
            }
            cartMapper.updateQuantity(existCart.getId(), newQuantity);
            return existCart.getId();
        }

        // 检查购物车数量
        int count = cartMapper.countByUserId(userId);
        if (count >= MAX_CART_COUNT) {
            throw new BusinessException(ResultCode.CART_ITEM_LIMIT);
        }

        // 新增购物车
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setFruitId(request.getFruitId());
        cart.setSpecId(request.getSpecId());
        cart.setQuantity(request.getQuantity());
        cart.setSelected(1);
        cartMapper.insert(cart);

        return cart.getId();
    }

    @Override
    @Transactional
    public void updateQuantity(Long id, Long userId, int quantity) {
        Cart cart = cartMapper.findByIdAndUserId(id, userId);
        if (cart == null) {
            throw new BusinessException(ResultCode.CART_ITEM_NOT_FOUND);
        }

        if (quantity < 1) {
            quantity = 1;
        }
        if (quantity > MAX_ITEM_QUANTITY) {
            quantity = MAX_ITEM_QUANTITY;
        }

        cartMapper.updateQuantity(id, quantity);
    }

    @Override
    @Transactional
    public void updateSelected(Long id, Long userId, boolean selected) {
        Cart cart = cartMapper.findByIdAndUserId(id, userId);
        if (cart == null) {
            throw new BusinessException(ResultCode.CART_ITEM_NOT_FOUND);
        }

        cartMapper.updateSelected(id, selected ? 1 : 0);
    }

    @Override
    @Transactional
    public void updateAllSelected(Long userId, boolean selected) {
        cartMapper.updateAllSelected(userId, selected ? 1 : 0);
    }

    @Override
    @Transactional
    public void deleteCartItem(Long id, Long userId) {
        Cart cart = cartMapper.findByIdAndUserId(id, userId);
        if (cart == null) {
            throw new BusinessException(ResultCode.CART_ITEM_NOT_FOUND);
        }

        cartMapper.delete(id);
    }

    @Override
    public List<CartVO> getCartItemsByIds(List<Long> ids, Long userId) {
        List<Cart> carts = cartMapper.findByIds(ids, userId);
        return carts.stream()
                .map(CartVO::fromCart)
                .collect(Collectors.toList());
    }
}
