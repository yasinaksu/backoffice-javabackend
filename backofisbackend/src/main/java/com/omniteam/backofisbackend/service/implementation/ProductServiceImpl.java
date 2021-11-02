package com.omniteam.backofisbackend.service.implementation;


import com.omniteam.backofisbackend.base.annotions.LogMethodCall;
import com.omniteam.backofisbackend.dto.PagedDataWrapper;
import com.omniteam.backofisbackend.dto.category.CategoryDTO;
import com.omniteam.backofisbackend.dto.customer.CustomerUpdateDto;
import com.omniteam.backofisbackend.dto.order.OrderDto;
import com.omniteam.backofisbackend.dto.product.ProductDto;
import com.omniteam.backofisbackend.dto.product.ProductGetAllDto;
import com.omniteam.backofisbackend.dto.product.ProductSaveRequestDTO;
import com.omniteam.backofisbackend.dto.product.ProductUpdateDTO;
import com.omniteam.backofisbackend.entity.*;
import com.omniteam.backofisbackend.enums.EnumLogIslemTipi;
import com.omniteam.backofisbackend.repository.*;
import com.omniteam.backofisbackend.repository.productspecification.ProductSpec;
import com.omniteam.backofisbackend.requests.ProductGetAllRequest;
import com.omniteam.backofisbackend.service.ProductService;
import com.omniteam.backofisbackend.shared.constant.ResultMessage;
import com.omniteam.backofisbackend.shared.mapper.ProductMapper;
import com.omniteam.backofisbackend.shared.result.DataResult;
import com.omniteam.backofisbackend.shared.result.Result;
import com.omniteam.backofisbackend.shared.result.SuccessDataResult;
import com.omniteam.backofisbackend.shared.result.SuccessResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AttributeTermRepository attributeTermRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductAttributeTermRepository productAttributeTermRepository;

    @Autowired
    private ProductPriceRepository productPriceRepository;

    @Autowired
    private SecurityVerificationServiceImpl securityVerificationService;

    @Autowired
    private LogServiceImpl logService;

    @LogMethodCall(value = "saveProductImageDB is started")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Result saveProductImageDB(MultipartFile file, Integer productId) throws IOException {

        Product product = productRepository.getById(productId);
        List<ProductImage> productImages = productImageRepository.findAllByProduct_ProductId(productId);
        if (productImages != null) {
            productImageRepository.deleteAllByProduct_ProductId(productId);

        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        ProductImage productImage = new ProductImage();
        //  productImage.setImage(file.getBytes());
        productImage.setImage(Base64.getEncoder().encodeToString(file.getBytes()));

        productImage.setProductImageName(fileName);

        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("//download")
                .path(fileName)
                .toUriString();

        productImage.setFilePath(url);
        productImage.setProduct(product);
        productImageRepository.save(productImage);
        logService.loglama(EnumLogIslemTipi.ProductImageAdd, securityVerificationService.inquireLoggedInUser());
        Method m = new Object() {
        }
                .getClass()
                .getEnclosingMethod();

        LogMethodCall logMethodCall = m.getAnnotation(LogMethodCall.class);
        return new SuccessResult(ResultMessage.PRODUCT_IMAGE_SAVE);


    }

    @LogMethodCall(value = "saveProductDB is stated")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Integer saveProductToDB(ProductSaveRequestDTO productSaveRequestDTO) {
        Product product = productMapper.mapToEntity(productSaveRequestDTO);
        List<ProductPrice> productPrice = productMapper.mapToEntities(productSaveRequestDTO.getProductPriceDTOS());

        productRepository.save(product);
        for (int i = 0; i < productPrice.size(); i++) {
            productPrice.get(i).setProduct(product);
        }
        productPriceRepository.saveAll(productPrice);

        List<ProductAttributeTerm> productAttributeTerms = productMapper.mapToProductAttributeTerm(productSaveRequestDTO.getProductAttributeTermDTOS());
        productAttributeTerms.stream().forEach(item -> item.setProduct(product));


        productAttributeTermRepository.saveAll(productAttributeTerms);
        logService.loglama(EnumLogIslemTipi.ProductAdd, securityVerificationService.inquireLoggedInUser());
        Method m = new Object() {
        }
                .getClass()
                .getEnclosingMethod();

        LogMethodCall logMethodCall = m.getAnnotation(LogMethodCall.class);
        return product.getProductId();


    }

    @LogMethodCall(value = "ProductGetAll is started")
    @Cacheable(cacheNames = "ProductGetAll")
    @Override
    public DataResult<PagedDataWrapper<ProductDto>> getAll(ProductGetAllRequest productGetAllRequest) throws InterruptedException {

        Thread.sleep(500L);
        Pageable pageable = PageRequest.of(productGetAllRequest.getPage(), productGetAllRequest.getSize());
        Page<Product> productPage =
                productRepository.findAll(
                        ProductSpec.getAllByFilter(
                                productGetAllRequest.getCategoryId(),
                                productGetAllRequest.getSearchKey(),
                                productGetAllRequest.getMinPrice(),
                                productGetAllRequest.getMaxPrice(),
                                productGetAllRequest.getStartDate(),
                                productGetAllRequest.getEndDate(),
                                productGetAllRequest.getAttributeIdsCollections()
                        ), pageable);


        List<ProductDto> productDtoList = productMapper.mapToDTOs(productPage.getContent());

        PagedDataWrapper<ProductDto> pagedDataWrapper = new PagedDataWrapper<>(
                productDtoList,
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isLast()
        );

        logService.loglama(EnumLogIslemTipi.ProductGetAll, securityVerificationService.inquireLoggedInUser());
        Method m = new Object() {
        }
                .getClass()
                .getEnclosingMethod();

        LogMethodCall logMethodCall = m.getAnnotation(LogMethodCall.class);
        return new SuccessDataResult<>(pagedDataWrapper);

    }

    @Transactional(readOnly = true)
    @LogMethodCall(value = "ProductGetById is started")
    public DataResult<ProductDto> getById(Integer productId) {
        Product product = productRepository.getById(productId);
        ProductDto productDto = productMapper.mapToDTO(product);
        //     logService.loglama(EnumLogIslemTipi.ProductGetById,securityVerificationService.inquireLoggedInUser());
        Method m = new Object() {
        }
                .getClass()
                .getEnclosingMethod();

        LogMethodCall logMethodCall = m.getAnnotation(LogMethodCall.class);
        return new SuccessDataResult<>(productDto);
    }


    @CacheEvict(cacheNames = "ProductGetAll", allEntries = true)
    public void clearProductGetAllCache() {
        System.out.println("cache temizlendi");
    }

    @LogMethodCall(value = "ProductUpdate is started")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, timeout = 800)
    public Result productUpdate(ProductUpdateDTO productUpdateDTO) {
        Product productToUpdate = productRepository.getById(productUpdateDTO.getProductId());
        if (Objects.nonNull(productToUpdate)) {
            if (productUpdateDTO.getProductName() != null) {
                productToUpdate.setProductName(productUpdateDTO.getProductName());
            }
            if (productUpdateDTO.getBarcode() != null) {
                productToUpdate.setBarcode(productUpdateDTO.getBarcode());
            }
            if (productUpdateDTO.getDescription() != null) {
                productToUpdate.setDescription(productUpdateDTO.getDescription());
            }

            if (productUpdateDTO.getShortDescription() != null) {
                productToUpdate.setShortDescription(productUpdateDTO.getShortDescription());
            }

            if (productUpdateDTO.getUnitsInStock() != null) {
                productToUpdate.setUnitsInStock(productUpdateDTO.getUnitsInStock());
            }

            if (productUpdateDTO.getUnitsInStock() != null) {
                productToUpdate.setUnitsInStock(productUpdateDTO.getUnitsInStock());
            }

            if (productUpdateDTO.getProductPriceDTOS() != null) {
                List<ProductPrice> productPriceList = productPriceRepository.getAllByProduct_ProductId(productToUpdate.getProductId());
                for (ProductPrice productPrice : productPriceList) {
                    productPrice.setIsActive(false);
                    this.productPriceRepository.save(productPrice);
                }

                List<ProductPrice> productPrices = productMapper.mapToEntities(productUpdateDTO.getProductPriceDTOS());
                productPrices.stream().forEach(item -> item.setProduct(productToUpdate));
                productPriceRepository.saveAll(productPrices);

            }

            if (productUpdateDTO.getCategoryId() != null) {
                Category category = categoryRepository.getById(productUpdateDTO.getCategoryId());
                productToUpdate.setCategory(category);

            }

            if (productUpdateDTO.getProductAttributeTermDTOS() != null) {

                productAttributeTermRepository.deleteAllByProduct_ProductId(productToUpdate.getProductId());
                List<ProductAttributeTerm> productAttributeTerms = productMapper.mapToProductAttributeTerm(productUpdateDTO.getProductAttributeTermDTOS());
                productAttributeTerms.stream().forEach(item -> item.setProduct(productToUpdate));
                productAttributeTermRepository.saveAll(productAttributeTerms);

            }


        }
        logService.loglama(EnumLogIslemTipi.ProductUpdate, securityVerificationService.inquireLoggedInUser());
        Method m = new Object() {
        }
                .getClass()
                .getEnclosingMethod();

        LogMethodCall logMethodCall = m.getAnnotation(LogMethodCall.class);
        productRepository.save(productToUpdate);
        return new SuccessResult(ResultMessage.PRODUCT_UPDATED);
    }
}
