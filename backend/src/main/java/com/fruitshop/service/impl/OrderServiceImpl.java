package com.fruitshop.service.impl;

import com.fruitshop.common.PageResult;
import com.fruitshop.common.ResultCode;
import com.fruitshop.dto.request.OrderCreateRequest;
import com.fruitshop.dto.request.OrderReviewRequest;
import com.fruitshop.entity.*;
import com.fruitshop.exception.BusinessException;
import com.fruitshop.mapper.*;
import com.fruitshop.service.OrderService;
import com.fruitshop.util.OrderNoUtil;
import com.fruitshop.vo.CartVO;
import com.fruitshop.vo.OrderVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderReviewMapper orderReviewMapper;
    private final CartMapper cartMapper;
    private final AddressMapper addressMapper;
    private final FruitMapper fruitMapper;
    private final FruitSpecMapper fruitSpecMapper;
    private final OrderNoUtil orderNoUtil;

    public OrderServiceImpl(OrderMapper orderMapper, OrderItemMapper orderItemMapper,
                            OrderReviewMapper orderReviewMapper,
                            CartMapper cartMapper, AddressMapper addressMapper,
                            FruitMapper fruitMapper, FruitSpecMapper fruitSpecMapper,
                            OrderNoUtil orderNoUtil) {
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.orderReviewMapper = orderReviewMapper;
        this.cartMapper = cartMapper;
        this.addressMapper = addressMapper;
        this.fruitMapper = fruitMapper;
        this.fruitSpecMapper = fruitSpecMapper;
        this.orderNoUtil = orderNoUtil;
    }

    @Override
    @Transactional
    public Map<String, Object> createOrder(Long userId, OrderCreateRequest request) {
        // 获取地址
        Address address = addressMapper.findByIdAndUserId(request.getAddressId(), userId);
        if (address == null) {
            throw new BusinessException(ResultCode.ADDRESS_NOT_FOUND);
        }

        // 获取购物车商品
        List<Cart> carts = cartMapper.findByIds(request.getCartIds(), userId);
        if (carts.isEmpty()) {
            throw new BusinessException("请选择要购买的商品");
        }

        // 计算总金额并创建订单项
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (Cart cart : carts) {
            // 检查商品状态
            Fruit fruit = fruitMapper.findById(cart.getFruitId());
            if (fruit == null || fruit.getStatus() == 0) {
                throw new BusinessException("商品 " + cart.getFruitName() + " 已下架");
            }

            // 检查库存
            int stock;
            BigDecimal price;
            if (cart.getSpecId() != null) {
                FruitSpec spec = fruitSpecMapper.findById(cart.getSpecId());
                stock = spec.getStock();
                price = spec.getPrice();
            } else {
                stock = fruit.getStock();
                price = fruit.getPrice();
            }

            if (stock < cart.getQuantity()) {
                throw new BusinessException("商品 " + cart.getFruitName() + " 库存不足");
            }

            // 计算小计
            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(cart.getQuantity()));
            totalAmount = totalAmount.add(subtotal);

            // 创建订单项
            OrderItem item = new OrderItem();
            item.setFruitId(cart.getFruitId());
            item.setFruitName(cart.getFruitName());
            item.setFruitImage(cart.getFruitImage());
            item.setSpecName(cart.getSpecName());
            item.setPrice(price);
            item.setQuantity(cart.getQuantity());
            item.setSubtotal(subtotal);
            orderItems.add(item);
        }

        // 创建订单
        Order order = new Order();
        order.setOrderNo(orderNoUtil.generateOrderNo());
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setPayAmount(totalAmount);
        order.setFreight(BigDecimal.ZERO);
        order.setStatus(Order.STATUS_UNPAID);
        order.setAddressName(address.getName());
        order.setAddressPhone(address.getPhone());
        order.setAddressDetail(address.getFullAddress());
        order.setRemark(request.getRemark());
        orderMapper.insert(order);

        // 保存订单项
        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
        }
        orderItemMapper.batchInsert(orderItems);

        // 扣减库存
        for (Cart cart : carts) {
            if (cart.getSpecId() != null) {
                fruitSpecMapper.updateStock(cart.getSpecId(), cart.getQuantity());
            } else {
                fruitMapper.updateStock(cart.getFruitId(), cart.getQuantity());
            }
        }

        // 删除购物车
        cartMapper.deleteByIds(request.getCartIds());

        Map<String, Object> result = new HashMap<>();
        result.put("orderId", order.getId());
        result.put("orderNo", order.getOrderNo());
        result.put("payAmount", order.getPayAmount());
        return result;
    }

    @Override
    public PageResult<OrderVO> getOrderList(Long userId, Integer status, int page, int size) {
        int offset = (page - 1) * size;
        List<Order> orders = orderMapper.findByUserId(userId, status, offset, size);
        long total = orderMapper.countByUserId(userId, status);

        List<OrderVO> orderVOs = orders.stream().map(order -> {
            List<OrderItem> items = orderItemMapper.findByOrderId(order.getId());
            order.setItems(items);
            OrderVO vo = OrderVO.fromOrder(order);
            if (order.getStatus() == Order.STATUS_COMPLETED) {
                int totalItemCount = items.size();
                int reviewedCount = orderReviewMapper.countByOrderId(order.getId());
                vo.setTotalItemCount(totalItemCount);
                vo.setReviewedCount(reviewedCount);
                if (reviewedCount == 0) {
                    vo.setReviewStatus(OrderVO.REVIEW_STATUS_NONE);
                    vo.setReviewed(false);
                } else if (reviewedCount < totalItemCount) {
                    vo.setReviewStatus(OrderVO.REVIEW_STATUS_PARTIAL);
                    vo.setReviewed(true);
                } else {
                    vo.setReviewStatus(OrderVO.REVIEW_STATUS_ALL);
                    vo.setReviewed(true);
                }
            }
            return vo;
        }).collect(Collectors.toList());

        return PageResult.of(orderVOs, total, page, size);
    }

    @Override
    public OrderVO getOrderDetail(Long id, Long userId) {
        Order order = orderMapper.findByIdAndUserId(id, userId);
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_FOUND);
        }

        List<OrderItem> items = orderItemMapper.findByOrderId(id);
        order.setItems(items);

        OrderVO vo = OrderVO.fromOrder(order);
        if (order.getStatus() == Order.STATUS_COMPLETED) {
            List<OrderReview> reviews = orderReviewMapper.findByOrderId(id);
            vo.setReviews(reviews);
            int totalItemCount = items.size();
            int reviewedCount = reviews.size();
            vo.setTotalItemCount(totalItemCount);
            vo.setReviewedCount(reviewedCount);
            if (reviewedCount == 0) {
                vo.setReviewStatus(OrderVO.REVIEW_STATUS_NONE);
                vo.setReviewed(false);
            } else if (reviewedCount < totalItemCount) {
                vo.setReviewStatus(OrderVO.REVIEW_STATUS_PARTIAL);
                vo.setReviewed(true);
            } else {
                vo.setReviewStatus(OrderVO.REVIEW_STATUS_ALL);
                vo.setReviewed(true);
            }
        }

        return vo;
    }

    @Override
    @Transactional
    public void payOrder(Long id, Long userId) {
        Order order = orderMapper.findByIdAndUserId(id, userId);
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_FOUND);
        }
        if (order.getStatus() != Order.STATUS_UNPAID) {
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR);
        }

        int result = orderMapper.pay(id);
        if (result == 0) {
            throw new BusinessException("支付失败");
        }

        // 更新销量
        List<OrderItem> items = orderItemMapper.findByOrderId(id);
        for (OrderItem item : items) {
            fruitMapper.updateSales(item.getFruitId(), item.getQuantity());
        }
    }

    @Override
    @Transactional
    public void shipOrder(Long id, String logisticsCompany, String logisticsNo) {
        Order order = orderMapper.findById(id);
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_FOUND);
        }
        if (order.getStatus() != Order.STATUS_UNSHIPPED) {
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR);
        }

        int result = orderMapper.shipWithLogistics(id, logisticsCompany, logisticsNo);
        if (result == 0) {
            throw new BusinessException("发货失败");
        }
    }

    @Override
    @Transactional
    public void cancelOrder(Long id, Long userId) {
        Order order = orderMapper.findByIdAndUserId(id, userId);
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_FOUND);
        }
        if (order.getStatus() != Order.STATUS_UNPAID) {
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR);
        }

        int result = orderMapper.cancel(id);
        if (result == 0) {
            throw new BusinessException("取消订单失败");
        }

        // 恢复库存 (简化处理，实际应该恢复到对应规格)
        // 这里省略恢复库存的逻辑
    }

    @Override
    @Transactional
    public void receiveOrder(Long id, Long userId) {
        Order order = orderMapper.findByIdAndUserId(id, userId);
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_FOUND);
        }
        if (order.getStatus() != Order.STATUS_UNRECEIVED) {
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR);
        }

        int result = orderMapper.receive(id);
        if (result == 0) {
            throw new BusinessException("确认收货失败");
        }
    }

    @Override
    @Transactional
    public void deleteOrder(Long id, Long userId) {
        Order order = orderMapper.findByIdAndUserId(id, userId);
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_FOUND);
        }
        if (order.getStatus() != Order.STATUS_COMPLETED && order.getStatus() != Order.STATUS_CANCELLED) {
            throw new BusinessException("只能删除已完成或已取消的订单");
        }

        // 先删除订单项，再删除订单（解决外键约束问题）
        orderItemMapper.deleteByOrderId(id);
        orderMapper.delete(id);
    }

    @Override
    @Transactional
    public void submitReview(Long orderId, Long userId, OrderReviewRequest request) {
        Order order = orderMapper.findByIdAndUserId(orderId, userId);
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_FOUND);
        }
        if (order.getStatus() != Order.STATUS_COMPLETED) {
            throw new BusinessException("只有已完成的订单才能评价");
        }

        OrderItem orderItem = orderItemMapper.findById(request.getOrderItemId());
        if (orderItem == null || !orderItem.getOrderId().equals(orderId)) {
            throw new BusinessException("订单项不存在");
        }

        OrderReview existingReview = orderReviewMapper.findByOrderItemId(request.getOrderItemId());
        if (existingReview != null) {
            throw new BusinessException("该商品已评价，不能重复评价");
        }

        OrderReview review = new OrderReview();
        review.setOrderId(orderId);
        review.setUserId(userId);
        review.setOrderItemId(request.getOrderItemId());
        review.setFruitId(orderItem.getFruitId());
        review.setRating(request.getRating());
        review.setContent(request.getContent());
        orderReviewMapper.insert(review);
    }

    @Override
    public List<OrderReview> getOrderReviews(Long orderId, Long userId) {
        Order order = orderMapper.findByIdAndUserId(orderId, userId);
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_FOUND);
        }
        return orderReviewMapper.findByOrderId(orderId);
    }
}
