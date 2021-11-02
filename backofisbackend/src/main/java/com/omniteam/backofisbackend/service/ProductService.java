package com.omniteam.backofisbackend.service;

import com.omniteam.backofisbackend.dto.PagedDataWrapper;
import com.omniteam.backofisbackend.dto.product.ProductDto;
import com.omniteam.backofisbackend.dto.product.ProductGetAllDto;
import com.omniteam.backofisbackend.dto.product.ProductSaveRequestDTO;
import com.omniteam.backofisbackend.dto.product.ProductUpdateDTO;
import com.omniteam.backofisbackend.requests.ProductGetAllRequest;
import com.omniteam.backofisbackend.shared.result.DataResult;
import com.omniteam.backofisbackend.shared.result.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    public Result saveProductImageDB(MultipartFile file,Integer productId) throws IOException;

    public Integer saveProductToDB(ProductSaveRequestDTO productSaveRequestDTO);

    public DataResult<PagedDataWrapper<ProductDto>> getAll(ProductGetAllRequest productGetAllRequest) throws InterruptedException;


    public DataResult<ProductDto> getById(Integer productId);

    public Result productUpdate(ProductUpdateDTO productUpdateDTO);

    public void clearProductGetAllCache();


}
