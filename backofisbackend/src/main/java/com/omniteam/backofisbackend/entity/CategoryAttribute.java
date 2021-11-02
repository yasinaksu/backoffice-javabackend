package com.omniteam.backofisbackend.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="category_attributes")
@Data
@NoArgsConstructor
@AllArgsConstructor()
public class CategoryAttribute extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_attribute_id")
    private Integer categoryAttributeId;

    @JoinColumn(name="category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;


    @JoinColumn(name="attribute_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Attribute attribute;


}
