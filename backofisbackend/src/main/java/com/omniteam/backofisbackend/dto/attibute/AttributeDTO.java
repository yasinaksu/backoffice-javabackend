package com.omniteam.backofisbackend.dto.attibute;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.omniteam.backofisbackend.entity.ProductAttributeTerm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AttributeDTO {

    private Integer attributeId;
    private String attributeTitle;
    private List<AttributeTermDTO> attributeTerms;
    //private List<ProductAttributeTerm> productAttributeTerms;
}
