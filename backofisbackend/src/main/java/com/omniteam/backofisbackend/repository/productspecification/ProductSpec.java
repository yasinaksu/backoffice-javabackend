package com.omniteam.backofisbackend.repository.productspecification;


import com.omniteam.backofisbackend.entity.*;
import com.omniteam.backofisbackend.entity.Order;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductSpec {
    public static Specification<Product> getAllByFilter(
            Integer categoryId,
            String searchKey,
            Double minPrice,
            Double maxPrice,
            LocalDateTime startDate,
            LocalDateTime endDate,
            List<List<Integer>> attributeIdsCollections) {
        Specification specification = Specification
                .where(searchProductBySearchKey(searchKey)
                        .and(getProductByCategoryId(categoryId))
                        .and(getProductByMinPrice(minPrice))
                        .and(getProductByMaxPrice(maxPrice))
                        .and(getProductByStartDate(startDate))
                        .and(getProductByEndDate(endDate)));
        if(attributeIdsCollections!=null && attributeIdsCollections.size()!=0){
            for(List<Integer> attributeIds:attributeIdsCollections){
                specification = specification.and(filterProductsByAttributeIdList(attributeIds));
            }
        }
        return specification;
    }

    public static Specification<Product> getProductByCategoryId(Integer categoryId){
        return (root, query, criteriaBuilder) -> {
            if(categoryId ==null || categoryId == 0){
                return criteriaBuilder.conjunction();
            }
            Predicate equalPredicate = criteriaBuilder.equal(root.get("category"), categoryId);
            return equalPredicate;
        };
    }
    public static Specification<Product> searchProductBySearchKey(String searchKey) {
        return (root, query, criteriaBuilder) -> {

            Predicate likePredicate = criteriaBuilder.or(criteriaBuilder.like(
                    root.get("productName"), "%"+searchKey+"%"
            ),criteriaBuilder.like(
                    root.get("barcode"), "%"+searchKey+"%"
            ),criteriaBuilder.like(
                    root.get("description"), "%"+searchKey+"%"
            ));


            return likePredicate;
        };
    }

    private static Specification<Product> getProductByMinPrice(Double minPrice) {
        return (root, query, criteriaBuilder) -> {

            if (minPrice == null) {
                return criteriaBuilder.conjunction();
            }
            ListJoin<Product, ProductPrice> productPriceListJoin = root.joinList("productPrices",JoinType.LEFT);

            Predicate betweenPredicate = criteriaBuilder.greaterThanOrEqualTo(productPriceListJoin.get("actualPrice"), minPrice);


            return betweenPredicate;
        };
    }

    private static Specification<Product> getProductByMaxPrice(Double maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (maxPrice == null) {
                return criteriaBuilder.conjunction();
            }
            ListJoin<Product, ProductPrice> productPriceListJoin = root.joinList("productPrices",JoinType.LEFT);

            Predicate betweenPredicate = criteriaBuilder.lessThanOrEqualTo(productPriceListJoin.get("actualPrice"), maxPrice);


            return betweenPredicate;
        };
    }


    private static Specification<Product> getProductByStartDate(LocalDateTime startDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null) {
                return criteriaBuilder.conjunction();
            }
            Predicate betweenPredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), startDate);
            return betweenPredicate;
        };
    }


    private static Specification<Product> getProductByEndDate(LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> {
            if (endDate == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"), endDate);
        };
    }

    public static Specification<Product> filterProductsByAttributeIdList(List<Integer> attributeIdList){
        return new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                if (attributeIdList == null || attributeIdList.size()==0) {
                    return criteriaBuilder.conjunction();
                }
                final List<Predicate> predicates = new ArrayList<>();
                final Subquery<ProductAttributeTerm> subquery = criteriaQuery.subquery(ProductAttributeTerm.class);
                final Root<ProductAttributeTerm> productAttributeTerm = subquery.from(ProductAttributeTerm.class);
                subquery.select(productAttributeTerm.get("product").get("productId"));
                subquery
                        .where(
                                criteriaBuilder.in(
                                        productAttributeTerm.get("attributeTerm").get("attributeTermId")
                                ).value(attributeIdList)
                        );
                predicates.add(root.get("productId").in(subquery));
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));

            }
        };
    }


}
