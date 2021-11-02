package com.omniteam.backofisbackend.dto.order;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderDetailDto {
    private Integer orderDetailId;
    private Integer orderId;
    private Integer productId;
    private String status;
    private String productName;
    private Double productPrice;
    private String productDescription;
    private String productShortDescription;
    private String productBarcode;
}
