package com.omniteam.backofisbackend.repository;


import com.omniteam.backofisbackend.entity.Product;
import com.omniteam.backofisbackend.entity.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice,Integer> {


    ProductPrice findByProduct_ProductId(Integer productId);

    ProductPrice findFirstByProductAndIsActiveOrderByCreatedDateDesc(Product product, Boolean isActive);

    List<ProductPrice>  deleteAllByProduct_ProductId(Integer productId);
    List<ProductPrice>  getAllByProduct_ProductId(Integer productId);
}
