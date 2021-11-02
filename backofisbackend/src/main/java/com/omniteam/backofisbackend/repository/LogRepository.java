package com.omniteam.backofisbackend.repository;

import com.omniteam.backofisbackend.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Log,Integer> {

}
