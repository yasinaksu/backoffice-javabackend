package com.omniteam.backofisbackend.repository.customspecification;

import com.omniteam.backofisbackend.entity.Order;
import com.omniteam.backofisbackend.entity.OrderDetail;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;

public class OrderSpec {
    public static Specification<Order> getAllByFilter(int customerId, String status, LocalDateTime startDate,LocalDateTime endDate) {
        return Specification
                .where(
                        getOrdersByCustomerId(customerId)
                        .and(getOrdersByStatus(status))
                        .and(getOrdersByStartDate(startDate))
                        .and(getOrdersByEndDate(endDate))
        );
    }

    public static Specification<Order> getOrdersByCustomerId(int customerId){
        return (root, query, criteriaBuilder) -> {
            ListJoin<Order, OrderDetail> orderDetailListJoin = root.joinList("orderDetails");
            if(customerId == 0){
                return criteriaBuilder.conjunction();
            }
            Predicate equalPredicate = criteriaBuilder.equal(root.get("customer"), customerId);
            return equalPredicate;
        };
    }

    public static Specification<Order> getOrdersByStatus(String status){
        return (root, query, criteriaBuilder) -> {
            if(status == null || status.isEmpty()){
                return criteriaBuilder.conjunction();
            }
            Predicate equalPredicate = criteriaBuilder.equal(root.get("status"), status);
            return equalPredicate;
        };
    }

    public static Specification<Order> getOrdersByStartDate(LocalDateTime startDate){
        return (root, query, criteriaBuilder) -> {
            if(startDate==null){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"),startDate);
        };
    }


    public static Specification<Order> getOrdersByEndDate(LocalDateTime endDate){
        return (root, query, criteriaBuilder) -> {
            if(endDate ==null){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"),endDate);
        };
    }
}
