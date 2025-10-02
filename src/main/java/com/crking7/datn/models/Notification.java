package com.crking7.datn.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String content;

    @Column
    private Boolean isRead;

    @Column
    private Boolean deliverStatus;

    @Column
    private int type;//thông báo đặt hàng 1, 2 thông báo huỷ đơn hàng, 3 thông báo hết sản phẩm

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Orders orders;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;
}
