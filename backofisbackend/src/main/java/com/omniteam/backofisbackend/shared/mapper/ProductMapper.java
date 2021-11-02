package com.omniteam.backofisbackend.shared.mapper;


import com.omniteam.backofisbackend.dto.product.*;
import com.omniteam.backofisbackend.entity.Product;
import com.omniteam.backofisbackend.entity.ProductAttributeTerm;
import com.omniteam.backofisbackend.entity.ProductImage;
import com.omniteam.backofisbackend.entity.ProductPrice;
import org.hibernate.id.uuid.StandardRandomStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;


@Mapper(
        componentModel = "spring"
)
public interface ProductMapper  {

    List<ProductGetAllDto> toProductGetAllDtoList(List<Product> products);
    @Mapping(target="categoryId",source="category.categoryId")
    @Mapping(target="categoryName",source="category.categoryName")
    @Mapping(target ="productImageDtoList",source = "productImages")
    @Mapping(target ="productPriceDTO",source = "productPrices")
    @Mapping(target ="productAttributeTermDTOS",source = "productAttributeTerms")
    ProductDto mapToDTO(Product product);


    @Mapping(source="categoryId",target="category.categoryId")
    @Mapping(source ="productPriceDTOS",target = "productPrices")
    Product mapToEntity(ProductSaveRequestDTO productSaveRequestDTO);


    void update(@MappingTarget Product product , ProductUpdateDTO productUpdateDTO);
    List<ProductDto> mapToDTOs(List<Product> products);


    ProductImageDto mapToProductImageDto(ProductImage productImage);
    List<ProductImageDto> mapToProductImageDtos(List<ProductImage> productImage);

    @Mapping(target = "isActive", expression = "java(productPrice.getIsActive())")
    @Mapping(target = "createdDate", expression = "java(productPrice.getIsActive())")
    List<ProductPriceDTO> mapToProductPriceDtos(List<ProductPrice> productPrice);
    List<ProductPrice> mapToEntities(List<ProductPriceDTO> productPriceDTOS);


    @Mapping(target = "product.productId", source = "productId")
    @Mapping(target = "attribute.attributeId", source = "attributeId")
    @Mapping(target = "attributeTerm.attributeTermId", source = "attributeTermId")

    ProductAttributeTerm mapToEntity (ProductAttributeTermDTO productAttributeTermDTO);

    List<ProductAttributeTerm> mapToProductAttributeTerm(List<ProductAttributeTermDTO> productAttributeTermDTOS);


    @Mapping(source = "product.productId", target = "productId")
    @Mapping(source = "attribute.attributeId", target = "attributeId")
    @Mapping(source = "attributeTerm.attributeTermId", target = "attributeTermId")

    ProductAttributeTermDTO mapToProductAttributeTermDto (ProductAttributeTerm productAttributeTerm);

    List<ProductAttributeTermDTO> mapToProductAttributeTermDtoList(List<ProductAttributeTerm> productAttributeTermList);
}
