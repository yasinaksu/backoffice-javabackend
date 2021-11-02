package com.omniteam.backofisbackend.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="attribute_terms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttributeTerm extends BaseEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attribute_term_id")
    private Integer attributeTermId;


    @Column(name = "attribute_value")
    private String attributeValue;

    @JoinColumn(name = "attribute_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Attribute attribute;

    @OneToMany(mappedBy = "attributeTerm")
    private List<ProductAttributeTerm> productAttributeTerms;





}
