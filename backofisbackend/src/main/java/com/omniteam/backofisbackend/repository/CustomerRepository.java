package com.omniteam.backofisbackend.repository;


import com.omniteam.backofisbackend.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer> {
    Page<Customer> findCustomersByFirstNameContainsOrLastNameContainsOrNationNumberContains(String firstName,String lastName,String nationNumber, Pageable pageable);
}
