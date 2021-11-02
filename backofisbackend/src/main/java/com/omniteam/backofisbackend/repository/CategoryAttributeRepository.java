package com.omniteam.backofisbackend.repository;


import com.omniteam.backofisbackend.entity.CategoryAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryAttributeRepository extends JpaRepository<CategoryAttribute,Integer> {
}
