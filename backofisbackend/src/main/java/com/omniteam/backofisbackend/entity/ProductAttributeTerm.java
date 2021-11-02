package com.omniteam.backofisbackend.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="product_attribute_terms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttributeTerm extends BaseEntity  {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_attribute_term_id")
    private Integer productAttributeTermsId;


    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;


    @JoinColumn(name = "attribute_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Attribute attribute;

    @JoinColumn(name = "attribute_term_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private AttributeTerm attributeTerm;
























}
