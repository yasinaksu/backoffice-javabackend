package com.omniteam.backofisbackend.service;

import com.omniteam.backofisbackend.dto.category.CategoryDTO;
import com.omniteam.backofisbackend.dto.category.CategoryGetAllDto;
import com.omniteam.backofisbackend.shared.result.DataResult;

import java.util.List;

public interface CategoryService {
   DataResult<List<CategoryGetAllDto>> getAll();

   DataResult<CategoryDTO> getById(int categoryId);
}
