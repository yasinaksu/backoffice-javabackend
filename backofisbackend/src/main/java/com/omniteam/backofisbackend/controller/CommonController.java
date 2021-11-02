package com.omniteam.backofisbackend.controller;

import com.omniteam.backofisbackend.dto.PagedDataWrapper;
import com.omniteam.backofisbackend.dto.city.CityDto;
import com.omniteam.backofisbackend.dto.country.CountryDto;
import com.omniteam.backofisbackend.dto.customer.CustomerGetAllDto;
import com.omniteam.backofisbackend.dto.district.DistrictDto;
import com.omniteam.backofisbackend.service.CityService;
import com.omniteam.backofisbackend.service.CountryService;
import com.omniteam.backofisbackend.service.DistrictService;
import com.omniteam.backofisbackend.shared.result.DataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/common")
public class CommonController {

    private final CountryService countryService;
    private final CityService cityService;
    private final DistrictService districtService;
    @Autowired
    public CommonController(CountryService countryService, CityService cityService, DistrictService districtService){
        this.countryService = countryService;
        this.cityService = cityService;
        this.districtService = districtService;
    }

    @GetMapping(path = "/getcountries")
    public ResponseEntity<DataResult<List<CountryDto>>> getCountries(
            @RequestParam(name = "countryname", required = false, defaultValue = ""
            ) String countryName){
        return ResponseEntity.ok(this.countryService.getAll(countryName));
    }

    @GetMapping(path = "/{countryid}/getcitiesbycountry")
    public ResponseEntity<DataResult<List<CityDto>>> getCitiesByCountry(@PathVariable(name = "countryid") int countryId){
        return ResponseEntity.ok(this.cityService.getCitiesByCountry(countryId));
    }

    @GetMapping(path = "/{cityid}/getdistrictsbycity")
    public ResponseEntity<DataResult<List<DistrictDto>>> getDistrictByCity(@PathVariable(name = "cityid") int cityId){
        return ResponseEntity.ok(this.districtService.getDistrictsByCity(cityId));
    }
}
