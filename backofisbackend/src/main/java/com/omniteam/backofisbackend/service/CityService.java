package com.omniteam.backofisbackend.service;

import com.omniteam.backofisbackend.dto.city.CityDto;
import com.omniteam.backofisbackend.shared.result.DataResult;

import java.util.List;

public interface CityService {
    DataResult<List<CityDto>> getCitiesByCountry(int countryId);
}
