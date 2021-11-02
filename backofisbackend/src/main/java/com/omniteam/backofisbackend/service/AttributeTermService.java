package com.omniteam.backofisbackend.service;

import com.omniteam.backofisbackend.dto.attibute.AttributeTermDTO;
import com.omniteam.backofisbackend.shared.result.DataResult;

import java.util.List;

public interface AttributeTermService {

    DataResult<List<AttributeTermDTO>> getByAttributeTermByAttribute(Integer attributeId) ;

}
