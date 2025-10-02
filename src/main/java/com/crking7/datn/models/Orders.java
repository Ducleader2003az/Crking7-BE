package com.crking7.datn.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "ORDERS", uniqueConstraints = { @UniqueConstraint(columnNames = { "codeOrders" }) })
@Builder
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String codeOrders;

    @Column
    private String userNameEmp;

    @Column
    private String fullName;

    @Column
    private String phone;

    @Column
    private String note;

    @Column
    private int shippingFee;

    @Column
    private String addressDetail;

    @Column
    private String province;

    @Column
    private String district;

    @Column
    private String wards;

    @Column
    private int type; //type = 0 là giỏ hàng, type = 1 là đã đặt hàng

    @Column
    private Boolean isCheckout;

    @Column
    private String paymentMethod;

    @Column
    private int status; //status 0 là hủy, 1 là chờ xét duyệt, 2 đã xác nhận, 3 đang vận chuyển, 4 đã giao, 5 là bị bom

    @Column
    private Date shipDate;

    @Column
    private Date orderDate;

    @Column
    private Date createDate;

    @Column
    private Date modifiedDate;

    @OneToMany(mappedBy = "orders", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToOne()
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "orders", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Notification> notifications = new ArrayList<>();
}
