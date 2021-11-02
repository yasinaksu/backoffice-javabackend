package com.omniteam.backofisbackend.service.implementation;

import com.omniteam.backofisbackend.dto.attibute.AttributeDTO;
import com.omniteam.backofisbackend.entity.Attribute;
import com.omniteam.backofisbackend.entity.AttributeTerm;
import com.omniteam.backofisbackend.entity.CategoryAttribute;
import com.omniteam.backofisbackend.entity.ProductAttributeTerm;
import com.omniteam.backofisbackend.repository.AttributeRepository;
import com.omniteam.backofisbackend.shared.mapper.AttributeMapper;
import com.omniteam.backofisbackend.shared.mapper.AttributeTermMapper;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AttributeServiceImplTest {

    @InjectMocks
    private AttributeServiceImpl attributeService;
    @Mock
    private AttributeRepository attributeRepository;
    @Spy
    private AttributeMapper attributeMapper = Mappers.getMapper(AttributeMapper.class);

    @Spy
    private AttributeTermMapper attributeTermMapper = Mappers.getMapper(AttributeTermMapper.class);

    @Test
    public void getAttributesByCategoryId() {
        Attribute attribute = new Attribute();
        attribute.setAttributeId(1);
        attribute.setAttributeTitle("Title");
        AttributeTerm attributeTerm = new AttributeTerm();
        attributeTerm.setAttributeTermId(1);
        attributeTerm.setProductAttributeTerms(Arrays.asList(new ProductAttributeTerm()));
        attribute.setAttributeTerms(Arrays.asList(attributeTerm));
        attribute.setCategoryAttributes(Arrays.asList(new CategoryAttribute()));
        attribute.setProductAttributeTerms(Arrays.asList(new ProductAttributeTerm()));

        Mockito.when(
                this.attributeRepository.findAttributesByCategoryId(
                        Mockito.anyInt()))
                .thenReturn(Arrays.asList(attribute));

        DataResult<List<AttributeDTO>> result = this.attributeService.getAttributesByCategoryId(1);

        Assertions.assertThat(result.getData()).hasSize(1);
    }
}