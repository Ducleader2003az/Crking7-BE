package com.crking7.datn.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column
    private String phoneCskh;

    @Column
    private String phone;

    @Column
    private String taxCode;

    @Column
    private String taxDate;

    @Column
    private String taxLocation;

    @Column
    private String address;

    @Column
    private Date createdDate;

    @Column
    private Date modifiedDate;

    @Column
    private int status;

    @OneToMany(mappedBy = "company", fetch = FetchType.EAGER)
    private List<SocialMedia> socialMedias;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
