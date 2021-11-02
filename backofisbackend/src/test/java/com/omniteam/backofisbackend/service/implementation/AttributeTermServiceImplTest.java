package com.omniteam.backofisbackend.service.implementation;

import com.omniteam.backofisbackend.dto.PagedDataWrapper;
import com.omniteam.backofisbackend.dto.attibute.AttributeTermDTO;
import com.omniteam.backofisbackend.dto.product.ProductDto;
import com.omniteam.backofisbackend.entity.AttributeTerm;
import com.omniteam.backofisbackend.entity.Product;
import com.omniteam.backofisbackend.repository.AttributeTermRepository;
import com.omniteam.backofisbackend.shared.mapper.AttributeTermMapper;
import com.omniteam.backofisbackend.shared.mapper.ProductMapper;
import com.omniteam.backofisbackend.shared.result.DataResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AttributeTermServiceImplTest {

    @InjectMocks
    private AttributeTermServiceImpl attributeTermService;

    @Mock
    private SecurityVerificationServiceImpl securityVerificationService;

    @Spy
    private AttributeTermMapper attributeTermMapper = Mappers.getMapper(AttributeTermMapper.class);

    @Mock
    private  LogServiceImpl logService;

    @Mock
    private AttributeTermRepository attributeTermRepository;

    @Test
    void getByAttributeTermByAttribute() {
        List<AttributeTerm> attributeTermList = new ArrayList<>();
        Mockito.when(attributeTermRepository.findAllByAttribute_AttributeId(Mockito.eq(1))).thenReturn(attributeTermList);
        DataResult<List<AttributeTermDTO>> result = this.attributeTermService.getByAttributeTermByAttribute(1);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.isSuccess()).isTrue();
    }
}
