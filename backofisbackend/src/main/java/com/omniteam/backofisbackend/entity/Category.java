package com.omniteam.backofisbackend.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="categories")
@Data
@NoArgsConstructor
@AllArgsConstructor()
public class Category extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer categoryId;


    @Column(name="category_name")
    private String categoryName;

    @OneToMany(mappedBy = "category")
    private List<Product> products;


    @OneToMany(mappedBy = "category")
    private List<CategoryAttribute> categoryAttributes;




}
