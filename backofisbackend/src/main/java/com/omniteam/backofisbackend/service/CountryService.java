package com.omniteam.backofisbackend.service;

import com.omniteam.backofisbackend.dto.country.CountryDto;
import com.omniteam.backofisbackend.shared.result.DataResult;

import java.util.List;

public interface CountryService {
    DataResult<List<CountryDto>> getAll(String countryName);
}
