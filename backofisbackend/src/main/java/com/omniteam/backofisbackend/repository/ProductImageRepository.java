package com.omniteam.backofisbackend.repository;


import com.omniteam.backofisbackend.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage,Integer> {

    List<ProductImage> findAllByProduct_ProductId(Integer productId);

    List<ProductImage> deleteAllByProduct_ProductId(Integer productId);
}
