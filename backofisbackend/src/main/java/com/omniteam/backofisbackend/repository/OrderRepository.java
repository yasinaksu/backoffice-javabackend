package com.omniteam.backofisbackend.repository;


import com.omniteam.backofisbackend.entity.Order;
import io.swagger.models.auth.In;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>, JpaSpecificationExecutor<Order> {
    Order findFirstByStatusAndIsActiveAndCustomer_CustomerIdOrderByCreatedDateDesc(String status,Boolean isActive, Integer customerId);

    Page<Order> findAll(@Nullable Specification<Order> var1, Pageable var2);

}
