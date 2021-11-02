package com.omniteam.backofisbackend.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="attributes")
@Data
@NoArgsConstructor
@AllArgsConstructor()
@JsonIgnoreProperties({"hibernateLazyInitializer","handler","attributes"})
public class Attribute extends  BaseEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attribute_id")
    private Integer attributeId;


    @Column(name="attribute_title")
    private  String attributeTitle;

    @OneToMany(mappedBy = "attribute" )
    private List<AttributeTerm> attributeTerms;


    @OneToMany(mappedBy = "attribute")
    private List<CategoryAttribute> categoryAttributes;

    @OneToMany(mappedBy = "attribute")
    private List<ProductAttributeTerm> productAttributeTerms;

}
