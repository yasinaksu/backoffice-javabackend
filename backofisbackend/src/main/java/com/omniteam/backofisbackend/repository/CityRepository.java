package com.omniteam.backofisbackend.repository;


import com.omniteam.backofisbackend.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City,Integer> {
    @Query(value = "select c from City c where c.country.countryId=:countryId")
    List<City> getCitiesByCountryId(int countryId);
}
