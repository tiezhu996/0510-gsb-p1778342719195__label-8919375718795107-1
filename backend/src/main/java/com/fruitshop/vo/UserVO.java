package com.fruitshop.vo;

public class UserVO {
    private Long id;
    private String phone;
    private String nickname;
    private String avatar;
    private OrderCountVO orderCount;

    public UserVO() {}

    public static UserVO fromUser(com.fruitshop.entity.User user) {
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setPhone(maskPhone(user.getPhone()));
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public OrderCountVO getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(OrderCountVO orderCount) {
        this.orderCount = orderCount;
    }

    public static class OrderCountVO {
        private int unpaid;
        private int unshipped;
        private int unreceived;
        private int completed;

        public int getUnpaid() {
            return unpaid;
        }

        public void setUnpaid(int unpaid) {
            this.unpaid = unpaid;
        }

        public int getUnshipped() {
            return unshipped;
        }

        public void setUnshipped(int unshipped) {
            this.unshipped = unshipped;
        }

        public int getUnreceived() {
            return unreceived;
        }

        public void setUnreceived(int unreceived) {
            this.unreceived = unreceived;
        }

        public int getCompleted() {
            return completed;
        }

        public void setCompleted(int completed) {
            this.completed = completed;
        }
    }
}
