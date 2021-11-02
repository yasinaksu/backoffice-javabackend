package com.omniteam.backofisbackend.repository;


import com.omniteam.backofisbackend.entity.Order;
import com.omniteam.backofisbackend.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail,Integer> {
    List<OrderDetail> getOrderDetailsByOrder(Order order);
    void deleteOrderDetailByOrderDetailId(Integer orderDetailId);
}
