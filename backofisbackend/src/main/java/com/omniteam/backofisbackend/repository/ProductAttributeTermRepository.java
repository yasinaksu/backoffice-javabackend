package com.omniteam.backofisbackend.repository;


import com.omniteam.backofisbackend.dto.product.ProductAttributeTermDTO;
import com.omniteam.backofisbackend.entity.ProductAttributeTerm;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductAttributeTermRepository extends JpaRepository<ProductAttributeTerm,Integer> {

     List<ProductAttributeTerm> findAllByProduct_ProductId(Integer productId);

     List<ProductAttributeTerm> deleteAllByProduct_ProductId(Integer productId);
    //Integer deleteById(ProductAttributeTermDTO productAttributeTermDTO);
}
