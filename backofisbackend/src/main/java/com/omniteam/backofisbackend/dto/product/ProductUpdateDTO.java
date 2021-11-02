package com.omniteam.backofisbackend.dto.product;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductUpdateDTO {

    private Integer productId;
    private String productName;
    private String description;
    private String shortDescription;
    private Integer unitsInStock;
    private Integer categoryId;
    private String barcode;
    private List<ProductAttributeTermDTO> productAttributeTermDTOS;
    private List<ProductPriceDTO> productPriceDTOS;






}
