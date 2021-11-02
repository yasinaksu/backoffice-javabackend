package com.omniteam.backofisbackend.service.implementation;


import com.omniteam.backofisbackend.base.annotions.LogMethodCall;
import com.omniteam.backofisbackend.dto.attibute.AttributeDTO;
import com.omniteam.backofisbackend.dto.attibute.AttributeTermDTO;
import com.omniteam.backofisbackend.entity.Attribute;
import com.omniteam.backofisbackend.entity.AttributeTerm;
import com.omniteam.backofisbackend.enums.EnumLogIslemTipi;
import com.omniteam.backofisbackend.repository.AttributeTermRepository;
import com.omniteam.backofisbackend.service.AttributeTermService;
import com.omniteam.backofisbackend.shared.mapper.AttributeTermMapper;
import com.omniteam.backofisbackend.shared.result.DataResult;
import com.omniteam.backofisbackend.shared.result.SuccessDataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.List;

@Service
public class AttributeTermServiceImpl implements AttributeTermService {

    @Autowired
    private SecurityVerificationServiceImpl securityVerificationService;
    @Autowired
    private  LogServiceImpl logService;

    @Autowired
    private AttributeTermRepository attributeTermRepository;
    @Autowired
    private AttributeTermMapper attributeTermMapper;
    @LogMethodCall(value = "getByAttributeTermByAttribute is started")
    public DataResult<List<AttributeTermDTO>> getByAttributeTermByAttribute(Integer attributeId)  {
        List<AttributeTerm> attributeTerm = attributeTermRepository.findAllByAttribute_AttributeId(attributeId);
        List<AttributeTermDTO> attributeTermDTOS = attributeTermMapper.mapToDTOs(attributeTerm);
        logService.loglama(EnumLogIslemTipi.AttributeTermsGet,securityVerificationService.inquireLoggedInUser());

        Method m = new Object() {}
                .getClass()
                .getEnclosingMethod();

        LogMethodCall logMethodCall =  m.getAnnotation(LogMethodCall.class);
        System.out.println(logMethodCall.value());
        return new SuccessDataResult<>(attributeTermDTOS);
    }


}
