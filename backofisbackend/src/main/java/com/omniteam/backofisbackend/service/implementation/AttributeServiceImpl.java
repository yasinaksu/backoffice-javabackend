package com.omniteam.backofisbackend.service.implementation;

import com.omniteam.backofisbackend.base.annotions.LogMethodCall;
import com.omniteam.backofisbackend.dto.attibute.AttributeDTO;
import com.omniteam.backofisbackend.dto.category.CategoryDTO;
import com.omniteam.backofisbackend.entity.Attribute;
import com.omniteam.backofisbackend.entity.Category;
import com.omniteam.backofisbackend.repository.AttributeRepository;
import com.omniteam.backofisbackend.service.AttributeService;
import com.omniteam.backofisbackend.shared.mapper.AttributeMapper;
import com.omniteam.backofisbackend.shared.result.DataResult;
import com.omniteam.backofisbackend.shared.result.SuccessDataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.List;

@Service
public class AttributeServiceImpl  implements AttributeService {

    @Autowired
    private AttributeRepository attributeRepository;
    @Autowired
    private AttributeMapper attributeMapper;
    @Override
    public DataResult<List<AttributeDTO>> getAttributesByCategoryId(int categoryId) {
        List<Attribute> attributeList = this.attributeRepository.findAttributesByCategoryId(categoryId);
        List<AttributeDTO> attributeDTOList = this.attributeMapper.toAttributeDTOList(attributeList);
        return new SuccessDataResult<>(attributeDTOList);
    }
}
