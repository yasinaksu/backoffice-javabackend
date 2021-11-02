package com.omniteam.backofisbackend.repository;

import com.omniteam.backofisbackend.entity.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormRepository extends JpaRepository<Form,Integer> {

}
