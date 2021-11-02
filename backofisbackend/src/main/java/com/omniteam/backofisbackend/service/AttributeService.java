package com.omniteam.backofisbackend.service;

import com.omniteam.backofisbackend.dto.attibute.AttributeDTO;
import com.omniteam.backofisbackend.shared.result.DataResult;

import java.util.List;


public interface AttributeService {
    DataResult<List<AttributeDTO>> getAttributesByCategoryId(int categoryId);

    //public AttributeDTO getById(Integer attributeId);
}
