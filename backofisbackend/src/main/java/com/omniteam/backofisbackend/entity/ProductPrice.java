package com.omniteam.backofisbackend.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="product_prices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPrice extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_price_id")
    private Integer productPriceId;


    @Column(name="actual_price")
    private Double actualPrice;


    @Column(name="discounted_price")
    private Double discountedPrice;


    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @OneToMany(mappedBy = "productPrice")
    private List<OrderDetail> orderDetails;
}
