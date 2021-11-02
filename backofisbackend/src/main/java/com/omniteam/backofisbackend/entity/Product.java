package com.omniteam.backofisbackend.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity  {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId ;


    @Column(name = "product_name")
    private  String productName;


    @Column(name = "description")
    private  String description;


    @Column(name = "short_description")
    private  String shortDescription;

    @Column(name = "units_in_stock")
    private  Integer unitsInStock;


    @Column(name = "barcode")
    private  String barcode;

    @OneToMany(mappedBy = "product")
    private List<OrderDetail> orderDetails;

    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<ProductAttributeTerm> productAttributeTerms;

    @OneToMany(mappedBy = "product")
    private List<ProductImage> productImages;

    @OneToMany(mappedBy = "product")
    private List<ProductPrice> productPrices;
}
