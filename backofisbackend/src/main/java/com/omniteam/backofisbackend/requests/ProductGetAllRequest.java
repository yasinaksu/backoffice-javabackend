package com.omniteam.backofisbackend.requests;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductGetAllRequest {

    private Integer productId ;
    private Integer categoryId ;
    private  String searchKey="";
    private Double minPrice;
    private  Double maxPrice;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int page;
    private int size;
    private List<List<Integer>> attributeIdsCollections;
}
