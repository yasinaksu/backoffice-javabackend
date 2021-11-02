package com.omniteam.backofisbackend.repository;

import com.omniteam.backofisbackend.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District,Integer> {
    @Query(
            value = "select d from District d where d.city.cityId =:cityId order by d.districtName"
    )
    List<District> getDistrictsByCity(int cityId);
}
