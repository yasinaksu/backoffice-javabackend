package com.omniteam.backofisbackend.shared.mapper;


import com.omniteam.backofisbackend.dto.attibute.AttributeDTO;
import com.omniteam.backofisbackend.dto.attibute.AttributeTermDTO;
import com.omniteam.backofisbackend.entity.AttributeTerm;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface AttributeTermMapper {
    List<AttributeTermDTO> mapToDTOs(List<AttributeTerm> attributeTerm);
    AttributeTermDTO toAttributeTermDto(AttributeTerm attributeTerm);
}
