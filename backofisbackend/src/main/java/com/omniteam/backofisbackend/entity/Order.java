package com.omniteam.backofisbackend.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity(name="orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order extends  BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name  ="order_id")
    private Integer orderId;


    @Column(name="status")
    private String status;


    @JoinColumn(name = "customer_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;



    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;


    public Order(Integer orderId) {
        this.orderId = orderId;
    }
}
