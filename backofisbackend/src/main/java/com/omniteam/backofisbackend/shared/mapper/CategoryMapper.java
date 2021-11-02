package com.omniteam.backofisbackend.shared.mapper;

import com.omniteam.backofisbackend.dto.category.CategoryDTO;
import com.omniteam.backofisbackend.dto.category.CategoryGetAllDto;
 import com.omniteam.backofisbackend.entity.Category;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface CategoryMapper {

    List<CategoryGetAllDto> toCategoryDtoList(List<Category> categories);
    CategoryGetAllDto toCategoryDto(Category category);
    CategoryDTO mapToDTO(Category category);

}
