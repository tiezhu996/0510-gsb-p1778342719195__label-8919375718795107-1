package com.fruitshop.vo;

import com.fruitshop.entity.Order;
import com.fruitshop.entity.OrderItem;
import com.fruitshop.entity.OrderReview;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderVO {
    private Long id;
    private String orderNo;
    private Integer status;
    private String statusText;
    private BigDecimal totalAmount;
    private BigDecimal payAmount;
    private BigDecimal freight;
    private String addressName;
    private String addressPhone;
    private String addressDetail;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime payTime;
    private LocalDateTime shipTime;
    private String logisticsCompany;
    private String logisticsNo;
    private LocalDateTime receiveTime;
    private LocalDateTime cancelTime;
    private Integer itemCount;
    private List<OrderItem> items;
    private Boolean reviewed;
    private Integer reviewStatus;
    private Integer reviewedCount;
    private Integer totalItemCount;
    private List<OrderReview> reviews;

    public static final int REVIEW_STATUS_NONE = 0;
    public static final int REVIEW_STATUS_PARTIAL = 1;
    public static final int REVIEW_STATUS_ALL = 2;

    public static OrderVO fromOrder(Order order) {
        OrderVO vo = new OrderVO();
        vo.setId(order.getId());
        vo.setOrderNo(order.getOrderNo());
        vo.setStatus(order.getStatus());
        vo.setStatusText(order.getStatusText());
        vo.setTotalAmount(order.getTotalAmount());
        vo.setPayAmount(order.getPayAmount());
        vo.setFreight(order.getFreight());
        vo.setAddressName(order.getAddressName());
        vo.setAddressPhone(maskPhone(order.getAddressPhone()));
        vo.setAddressDetail(order.getAddressDetail());
        vo.setRemark(order.getRemark());
        vo.setCreateTime(order.getCreateTime());
        vo.setPayTime(order.getPayTime());
        vo.setShipTime(order.getShipTime());
        vo.setLogisticsCompany(order.getLogisticsCompany());
        vo.setLogisticsNo(order.getLogisticsNo());
        vo.setReceiveTime(order.getReceiveTime());
        vo.setCancelTime(order.getCancelTime());
        vo.setItemCount(order.getItemCount());
        vo.setItems(order.getItems());
        vo.setReviewed(false);
        vo.setReviewStatus(REVIEW_STATUS_NONE);
        vo.setReviewedCount(0);
        vo.setTotalItemCount(0);
        return vo;
    }

    private static String maskPhone(String phone) {
        if (phone == null || phone.length() != 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public BigDecimal getFreight() {
        return freight;
    }

    public void setFreight(BigDecimal freight) {
        this.freight = freight;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getAddressPhone() {
        return addressPhone;
    }

    public void setAddressPhone(String addressPhone) {
        this.addressPhone = addressPhone;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getPayTime() {
        return payTime;
    }

    public void setPayTime(LocalDateTime payTime) {
        this.payTime = payTime;
    }

    public LocalDateTime getShipTime() {
        return shipTime;
    }

    public void setShipTime(LocalDateTime shipTime) {
        this.shipTime = shipTime;
    }

    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    public void setLogisticsCompany(String logisticsCompany) {
        this.logisticsCompany = logisticsCompany;
    }

    public String getLogisticsNo() {
        return logisticsNo;
    }

    public void setLogisticsNo(String logisticsNo) {
        this.logisticsNo = logisticsNo;
    }

    public LocalDateTime getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(LocalDateTime receiveTime) {
        this.receiveTime = receiveTime;
    }

    public LocalDateTime getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(LocalDateTime cancelTime) {
        this.cancelTime = cancelTime;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public Boolean getReviewed() {
        return reviewed;
    }

    public void setReviewed(Boolean reviewed) {
        this.reviewed = reviewed;
    }

    public List<OrderReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<OrderReview> reviews) {
        this.reviews = reviews;
    }

    public Integer getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(Integer reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public Integer getReviewedCount() {
        return reviewedCount;
    }

    public void setReviewedCount(Integer reviewedCount) {
        this.reviewedCount = reviewedCount;
    }

    public Integer getTotalItemCount() {
        return totalItemCount;
    }

    public void setTotalItemCount(Integer totalItemCount) {
        this.totalItemCount = totalItemCount;
    }
}
