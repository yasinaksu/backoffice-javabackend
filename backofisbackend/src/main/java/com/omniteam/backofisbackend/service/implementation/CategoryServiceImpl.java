package com.omniteam.backofisbackend.service.implementation;

import com.omniteam.backofisbackend.base.annotions.LogMethodCall;
import com.omniteam.backofisbackend.dto.category.CategoryDTO;
import com.omniteam.backofisbackend.dto.category.CategoryGetAllDto;
import com.omniteam.backofisbackend.entity.Category;
import com.omniteam.backofisbackend.enums.EnumLogIslemTipi;
import com.omniteam.backofisbackend.repository.CategoryRepository;
import com.omniteam.backofisbackend.service.CategoryService;
import com.omniteam.backofisbackend.shared.mapper.CategoryMapper;
import com.omniteam.backofisbackend.shared.result.DataResult;
import com.omniteam.backofisbackend.shared.result.SuccessDataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {


    @Autowired
    private SecurityVerificationServiceImpl securityVerificationService;

    @Autowired
    private  LogServiceImpl logService;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper categoryMapper;



    @LogMethodCall(value = "getAll is started")
    @Override
    public DataResult<List<CategoryGetAllDto>> getAll() {
        List<Category> categories = this.categoryRepository.findAll();
        List<CategoryGetAllDto> categoryGetAllDtoList = this.categoryMapper.toCategoryDtoList(categories);
        logService.loglama(EnumLogIslemTipi.CategoryGetAll,securityVerificationService.inquireLoggedInUser());
        Method m = new Object() {}
                .getClass()
                .getEnclosingMethod();

        LogMethodCall logMethodCall =  m.getAnnotation(LogMethodCall.class);

        return new SuccessDataResult<>(categoryGetAllDtoList);
    }

    @LogMethodCall(value = "Category getById is started")
    @Override
    public DataResult<CategoryDTO> getById(int categoryId) {
        Category category = this.categoryRepository.getById(categoryId);
        CategoryDTO categoryDTO = this.categoryMapper.mapToDTO(category);
        logService.loglama(EnumLogIslemTipi.CategoryGetById,securityVerificationService.inquireLoggedInUser());
        Method m = new Object() {}
                .getClass()
                .getEnclosingMethod();

        LogMethodCall logMethodCall =  m.getAnnotation(LogMethodCall.class);
        return new SuccessDataResult<>(categoryDTO);
    }


}
