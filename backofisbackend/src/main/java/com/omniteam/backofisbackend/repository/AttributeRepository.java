package com.omniteam.backofisbackend.repository;


import com.omniteam.backofisbackend.entity.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute,Integer> {
    @Query(value = "select distinct a from CategoryAttribute ca, Attribute a, AttributeTerm at" +
            " where ca.category.categoryId=:categoryId" +
            " and ca.attribute.attributeId = a.attributeId" +
            " and a.attributeId = at.attribute.attributeId")
    List<Attribute> findAttributesByCategoryId(int categoryId);

}
