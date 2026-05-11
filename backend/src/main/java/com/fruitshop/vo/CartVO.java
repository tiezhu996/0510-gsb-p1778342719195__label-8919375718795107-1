package com.fruitshop.vo;

import com.fruitshop.entity.Cart;

import java.math.BigDecimal;

public class CartVO {
    private Long id;
    private Long fruitId;
    private String fruitName;
    private String fruitImage;
    private Long specId;
    private String specName;
    private BigDecimal price;
    private Integer quantity;
    private Boolean selected;
    private Integer stock;
    private Integer status;

    public static CartVO fromCart(Cart cart) {
        CartVO vo = new CartVO();
        vo.setId(cart.getId());
        vo.setFruitId(cart.getFruitId());
        vo.setFruitName(cart.getFruitName());
        vo.setFruitImage(cart.getFruitImage());
        vo.setSpecId(cart.getSpecId());
        vo.setSpecName(cart.getSpecName());
        vo.setPrice(cart.getPrice());
        vo.setQuantity(cart.getQuantity());
        vo.setSelected(cart.getSelected() != null && cart.getSelected() == 1);
        vo.setStock(cart.getStock());
        vo.setStatus(cart.getFruitStatus());
        return vo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFruitId() {
        return fruitId;
    }

    public void setFruitId(Long fruitId) {
        this.fruitId = fruitId;
    }

    public String getFruitName() {
        return fruitName;
    }

    public void setFruitName(String fruitName) {
        this.fruitName = fruitName;
    }

    public String getFruitImage() {
        return fruitImage;
    }

    public void setFruitImage(String fruitImage) {
        this.fruitImage = fruitImage;
    }

    public Long getSpecId() {
        return specId;
    }

    public void setSpecId(Long specId) {
        this.specId = specId;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
