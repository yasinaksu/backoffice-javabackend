package com.omniteam.backofisbackend.repository;

import com.omniteam.backofisbackend.entity.FormControl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormControlRepository extends JpaRepository<FormControl, Integer> {
    List<FormControl> getFormControlsByForm_FormIdOrderBySorterAsc(Integer formId);
}
