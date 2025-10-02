package com.crking7.datn.web.dto.response;

import lombok.Data;


@Data
public class NotificationResponse {
    private long id;
    private String content;
    private Boolean isRead;
    private Boolean deliverStatus;
    private int type;//thông báo đặt hàng 1, 2 thông báo huỷ đơn hàng, 3 thông báo hết sản phẩm
    private Long orders;
    private Long product;
}
