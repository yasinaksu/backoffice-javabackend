package com.omniteam.backofisbackend.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="product_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_image_id")
    private Integer productImageId;

    @Column(name = "short_description")
    private String shortDescrition;

    @Column(name = "file_path")
    private String filePath;



    @Column(name = "image")
    private String image;

    @Column(name = "product_image_name")
    private String productImageName;


    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;



}
