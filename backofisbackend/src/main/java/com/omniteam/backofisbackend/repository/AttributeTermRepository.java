package com.omniteam.backofisbackend.repository;

import com.omniteam.backofisbackend.entity.AttributeTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttributeTermRepository extends JpaRepository<AttributeTerm,Integer> {


    List<AttributeTerm> findAllByAttribute_AttributeId(Integer attributeId);

 }
