package com.fruitshop.vo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fruitshop.entity.Fruit;
import com.fruitshop.entity.FruitSpec;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class FruitVO {
    private Long id;
    private Long categoryId;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private String unit;
    private Integer stock;
    private Integer sales;
    private String mainImage;
    private List<String> images;
    private String detail;
    private String origin;
    private Boolean isHot;
    private Boolean isNew;
    private List<FruitSpec> specs;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static FruitVO fromFruit(Fruit fruit) {
        FruitVO vo = new FruitVO();
        vo.setId(fruit.getId());
        vo.setCategoryId(fruit.getCategoryId());
        vo.setName(fruit.getName());
        vo.setDescription(fruit.getDescription());
        vo.setPrice(fruit.getPrice());
        vo.setOriginalPrice(fruit.getOriginalPrice());
        vo.setUnit(fruit.getUnit());
        vo.setStock(fruit.getStock());
        vo.setSales(fruit.getSales());
        vo.setMainImage(fruit.getMainImage());
        vo.setDetail(fruit.getDetail());
        vo.setOrigin(fruit.getOrigin());
        vo.setIsHot(fruit.getIsHot() != null && fruit.getIsHot() == 1);
        vo.setIsNew(fruit.getIsNew() != null && fruit.getIsNew() == 1);
        vo.setSpecs(fruit.getSpecs());

        // 解析images JSON
        if (fruit.getImages() != null && !fruit.getImages().isEmpty()) {
            try {
                vo.setImages(objectMapper.readValue(fruit.getImages(), new TypeReference<List<String>>() {}));
            } catch (Exception e) {
                vo.setImages(Collections.emptyList());
            }
        } else {
            vo.setImages(Collections.emptyList());
        }

        return vo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Boolean getIsHot() {
        return isHot;
    }

    public void setIsHot(Boolean isHot) {
        this.isHot = isHot;
    }

    public Boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    public List<FruitSpec> getSpecs() {
        return specs;
    }

    public void setSpecs(List<FruitSpec> specs) {
        this.specs = specs;
    }
}
