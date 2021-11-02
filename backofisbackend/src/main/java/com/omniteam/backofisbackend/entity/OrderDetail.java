package com.omniteam.backofisbackend.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity(name = "order_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail extends BaseEntity  {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Integer orderDetailId;


    @Column(name="status")
    private String status;

    @JoinColumn(name = "order_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;


    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @JoinColumn(name = "product_price_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductPrice productPrice;




}
