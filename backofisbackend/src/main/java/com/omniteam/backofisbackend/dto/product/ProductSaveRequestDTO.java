package com.omniteam.backofisbackend.dto.product;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductSaveRequestDTO implements Serializable {

    private Integer productId;
    private Integer categoryId;
    private String productName;
    private String description;
    private String shortDescription;
    private Integer unitsInStock;
    private String barcode;
    private List<ProductAttributeTermDTO> productAttributeTermDTOS;
    private List<ProductPriceDTO> productPriceDTOS;
}
