package com.crking7.datn.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "ADDRESS")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String phone;

    @Column
    private String addressDetail;

    @Column
    private String province;

    @Column
    private String district;

    @Column
    private String wards;

    @Column
    private Date createdDate;

    @Column
    private Date modifiedDate;

    @Column
    private int focus;

    @Column
    private int status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "address")
    private List<Orders> orders = new ArrayList<>();
}
