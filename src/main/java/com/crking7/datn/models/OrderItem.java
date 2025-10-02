    package com.crking7.datn.models;

    import com.fasterxml.jackson.annotation.JsonBackReference;
    import jakarta.persistence.*;
    import lombok.*;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
@Builder
    @Entity
    @Table(name = "Order_Item")
    public class OrderItem {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        @Column
        private int quantity;

        @Column
        private int sellPrice;

        @Column
        private String productName;

        @Column
        private String valueColor;

        @Column
        private String valueSize;

        @ManyToOne
        @JoinColumn(name = "order_id")
        private Orders orders;
    }
