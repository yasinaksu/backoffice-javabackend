package com.omniteam.backofisbackend.service;

import com.omniteam.backofisbackend.dto.district.DistrictDto;
import com.omniteam.backofisbackend.shared.result.DataResult;

import java.util.List;

public interface DistrictService {
    DataResult<List<DistrictDto>> getDistrictsByCity(int cityId);
}
