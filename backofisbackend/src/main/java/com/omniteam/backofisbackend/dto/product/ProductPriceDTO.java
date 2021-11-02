package com.omniteam.backofisbackend.dto.product;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductPriceDTO {
    @JsonIgnore
    private Integer productPriceId;
    private Double actualPrice;
    private Double discountedPrice;
    private Boolean isActive;
    private LocalDateTime createdDate;
}
