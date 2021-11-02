package com.omniteam.backofisbackend.repository.userspecification;

import com.omniteam.backofisbackend.entity.Order;
import com.omniteam.backofisbackend.entity.Product;
import com.omniteam.backofisbackend.entity.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.Locale;

public class UserSpec {

    public static Specification<User> searchUsersBySearchKey(String searchKey) {
        return (root, query, criteriaBuilder) -> {

            Predicate likePredicate = criteriaBuilder.or(criteriaBuilder.like(
                    criteriaBuilder.lower(
                            root.get("firstName")
                    ), "%"+searchKey.toLowerCase(new Locale("tr", "TR"))+"%"
            ),criteriaBuilder.like(
                    criteriaBuilder.lower(
                            root.get("lastName")
                    ), "%"+searchKey.toLowerCase(new Locale("tr", "TR"))+"%"
            ),criteriaBuilder.like(
                    criteriaBuilder.lower(
                            root.get("title")
                    ), "%"+searchKey.toLowerCase(new Locale("tr", "TR"))+"%"
            ),criteriaBuilder.like(
                    criteriaBuilder.lower(
                            root.get("email")
                    ), "%"+searchKey.toLowerCase(new Locale("tr", "TR"))+"%"
            ),criteriaBuilder.like(
                    criteriaBuilder.lower(
                            root.get("phoneNumber")
                    ), "%"+searchKey.toLowerCase(new Locale("tr", "TR"))+"%"
            ));

            return likePredicate;
        };
    }
}
