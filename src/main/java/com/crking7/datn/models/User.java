package com.crking7.datn.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter

@Table(name = "user",uniqueConstraints = { @UniqueConstraint(columnNames = { "username" }),
        @UniqueConstraint(columnNames = { "email" }) })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private String email;

    @Column
    private LocalDateTime otpGeneratedTime;

    @Column
    private String otp;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String phone;

    @Column
    private Date createdDate;

    @Column
    private Date modifiedDate;

    @Column(nullable = false)
    private int status;

    @ManyToMany(targetEntity = Role.class,fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Address> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "productAuthor")
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Company> companies = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Orders> orders = new ArrayList<>();
}