package com.omniteam.backofisbackend.service.implementation;

import com.omniteam.backofisbackend.dto.PagedDataWrapper;
import com.omniteam.backofisbackend.dto.product.*;
import com.omniteam.backofisbackend.entity.*;
import com.omniteam.backofisbackend.repository.*;
import com.omniteam.backofisbackend.requests.ProductGetAllRequest;
import com.omniteam.backofisbackend.shared.constant.ResultMessage;
import com.omniteam.backofisbackend.shared.mapper.ProductMapper;
import com.omniteam.backofisbackend.shared.result.DataResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;


@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Spy
     private ProductMapper productMapper = Mappers.getMapper(ProductMapper.class); ;

    @Mock
    private ProductPriceRepository productPriceRepository;

    @Mock
    private ProductAttributeTermRepository productAttributeTermRepository;

    @Mock
    private ProductImageRepository productImageRepository;

    @Mock
    private LogServiceImpl logService;
    @Mock
    private CategoryRepository  categoryRepository;
    @Mock
    private SecurityVerificationServiceImpl securityVerificationService;

    private MockHttpServletRequest mockRequest;


    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    @BeforeEach
    void setupEach()
    {
        mockRequest = new MockHttpServletRequest();
        mockRequest.setContextPath("/");

        ServletRequestAttributes attrs = new ServletRequestAttributes(mockRequest);

        RequestContextHolder.setRequestAttributes(attrs);
    }

    @Test
    @DisplayName(value = "Must Find Product by Product ID")
   public void getById() {
        Product product = new Product();
        product.setProductId(11);

        Mockito.when(productRepository.getById(Mockito.any())).thenReturn(product);

        ProductDto productDto = new ProductDto();
        productDto.setProductId(1);

        Mockito.when(productMapper.mapToDTO(Mockito.any(Product.class))).thenReturn(productDto);

        DataResult<ProductDto> serviceResult = productService.getById(11);

        assertEquals(serviceResult.getData(),productDto);
    }

    @Test
   public void when_productSaveRequestDTO_Expect_saveProductDB() {

        ProductSaveRequestDTO productSaveRequestDTO =  new ProductSaveRequestDTO();
        List<ProductPriceDTO> productPriceDTOs = new ArrayList<>();
        List<ProductAttributeTermDTO> productAttributeTermDTOS = new ArrayList<>();
        ProductAttributeTermDTO productAttributeTermDTO = new ProductAttributeTermDTO();
        ProductPriceDTO productPriceDTO = new ProductPriceDTO();

        productPriceDTO.setProductPriceId(175);
        productPriceDTO.setActualPrice(123.123);
        productPriceDTO.setDiscountedPrice(123.123);
        productPriceDTO.setIsActive(true);
        productPriceDTO.setCreatedDate(LocalDateTime.now());
        productPriceDTOs.add(productPriceDTO);

        productAttributeTermDTO.setProductId(100);
        productAttributeTermDTO.setAttributeId(2);
        productAttributeTermDTO.setAttributeTermId(2);
        productAttributeTermDTOS.add(productAttributeTermDTO);


        productSaveRequestDTO.setProductPriceDTOS(productPriceDTOs);
        productSaveRequestDTO.setProductId(100);
        productSaveRequestDTO.setProductName("aaaaaaa");
        productSaveRequestDTO.setBarcode("22222");
        productSaveRequestDTO.setCategoryId(4);
        productSaveRequestDTO.setDescription("123123");
        productSaveRequestDTO.setShortDescription("123");
        productSaveRequestDTO.setUnitsInStock(21231);
        productSaveRequestDTO.setProductAttributeTermDTOS(productAttributeTermDTOS);


        Product product = productMapper.mapToEntity(productSaveRequestDTO);
        product.setProductId(100);
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);

        List<ProductAttributeTerm> productAttributeTerms =productMapper.mapToProductAttributeTerm(productSaveRequestDTO.getProductAttributeTermDTOS());
        productAttributeTerms.stream().forEach(item->item.setProduct(product));
        Mockito.when(productAttributeTermRepository.saveAll(Mockito.anyList())).thenReturn(productAttributeTerms);


        List<ProductPrice> productPrice = productMapper.mapToEntities(productSaveRequestDTO.getProductPriceDTOS());
        Mockito.when(productPriceRepository.saveAll(Mockito.anyList())).thenReturn(productPrice);


        Integer servisResult = productService.saveProductToDB(productSaveRequestDTO);

       assertEquals(servisResult,product.getProductId());

    }

     @Test
    public void Image_Expect_saveProductImageDB() throws IOException {
      Product product = new Product();
      product.setProductId(84);
      Mockito.when(productRepository.getById(Mockito.any())).thenReturn(product);
         List<ProductImage> productImages =new ArrayList<>();
         Mockito.when(productImageRepository.findAllByProduct_ProductId(Mockito.eq(84))).thenReturn(productImages);

         if (productImages!=null){
             Mockito.when(productImageRepository.deleteAllByProduct_ProductId(Mockito.eq(84))).thenReturn(productImages);
         }

        // MockMultipartFile mockMultipartFile = new MockMultipartFile();
         MockMultipartFile file
                 = new MockMultipartFile(
                 "file",
                 "hello.txt",
                 MediaType.TEXT_PLAIN_VALUE,
                 "Hello, World!".getBytes()
         );


         String fileName = StringUtils.cleanPath(file.getOriginalFilename());

         ProductImage productImage = new ProductImage();

         productImage.setImage(Base64.getEncoder().encodeToString(file.getBytes()));

         productImage.setProductImageName(fileName);

         String url ="//downloadhello.txt";
//         String url =new MockHttpServletRequestBuilder(HttpMethod.POST,"//downloadhello.txt").contextPath().pathInfo("//download").pathInfo(fileName).toString();

         productImage.setFilePath(url);
         productImage.setProduct(product);

         Mockito.when(productImageRepository.save(Mockito.any(ProductImage.class))).thenReturn(productImage);

        // String message = ResultMessage.PRODUCT_IMAGE_SAVE;
         String serviceResult = productService.saveProductImageDB(file,product.getProductId()).getMessage();

         assertEquals(serviceResult,ResultMessage.PRODUCT_IMAGE_SAVE);
     }

    @Test
    public void update(){

        ProductUpdateDTO productUpdateDTO =new ProductUpdateDTO();
        List<ProductPriceDTO>  productPrices = new ArrayList();
        productPrices.add(new ProductPriceDTO());
        ProductPrice productPrice = new ProductPrice();
        Category category = new Category();
       List<ProductAttributeTermDTO> productAttributeTermDTOS = new ArrayList<>();
       List<ProductAttributeTerm> productAttributeTerms = new ArrayList<>();
        List<ProductPrice> productPriceList = new ArrayList<>();
        productPriceList.add(new ProductPrice());
        productUpdateDTO.setProductId(84);
        productUpdateDTO.setProductName("test");
        productUpdateDTO.setBarcode("987");
        productUpdateDTO.setDescription("telefon");
        productUpdateDTO.setShortDescription("tel");
        productUpdateDTO.setUnitsInStock(5);
        productUpdateDTO.setCategoryId(4);
        productUpdateDTO.setProductPriceDTOS(productPrices);
        productUpdateDTO.setProductAttributeTermDTOS(productAttributeTermDTOS);

        Product productUpdate = new Product();

        productPrice.setProductPriceId(8);
        productPrice.setIsActive(false);
        Mockito.when(productRepository.getById(Mockito.eq(84))).thenReturn(productUpdate);
           assertNotNull(productUpdate);
           assertNotNull(productUpdateDTO.getProductName());
           assertNotEquals(null,productUpdateDTO.getProductPriceDTOS());



        productUpdate.setProductName(productUpdateDTO.getProductName());

        Mockito.when(categoryRepository.getById(Mockito.eq(4))).thenReturn(category);

        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(productUpdate);
        Mockito.when(productAttributeTermRepository.saveAll(Mockito.anyList())).thenReturn(productAttributeTerms);
        Mockito.when(productPriceRepository.getAllByProduct_ProductId(Mockito.any())).thenReturn(productPriceList);
        //Mockito.when(productPriceRepository.save(Mockito.any(ProductPrice.class))).thenReturn(productPrice);



        String serviceResult = productService.productUpdate(productUpdateDTO).getMessage();
        assertEquals(serviceResult,ResultMessage.PRODUCT_UPDATED);
        Assertions.assertThat(serviceResult).isNotNull();


    }

    @Test
    public void getAll() throws InterruptedException {
         List<Product> products = new ArrayList<>();

         for (int i =0; i<10;i++){
           //   products.add(new Product());
             Product product =new Product();
             product.setProductPrices(Arrays.asList(new ProductPrice()));
             product.setProductAttributeTerms(Arrays.asList(new ProductAttributeTerm()));
             product.setProductImages(Arrays.asList(new ProductImage()));
             products.add(product);
         }


        Page<Product> productPage = new PageImpl<Product>(products,PageRequest.of(0,5),10);
        //Mockito.when(userRepository.findAll(PageRequest.of(0, 20))).thenReturn(new PageImpl<>(mockedUsers));

        Mockito.when(
                   this.productRepository.findAll(
                            Mockito.any(Specification.class),
                           Mockito.any(Pageable.class)
                            )
           ).thenReturn(productPage);

       ProductGetAllRequest productGetAllRequest =new ProductGetAllRequest();
       productGetAllRequest.setPage(0);
       productGetAllRequest.setSize(5);

        DataResult<PagedDataWrapper<ProductDto>> result = this.productService.getAll(productGetAllRequest);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.isSuccess()).isTrue();
        Assertions.assertThat(result.getData().getContent()).isNotNull();
        Assertions.assertThat(result.getData().getContent()).hasSize(10);
        Assertions.assertThat(result.getData().getSize()).isEqualTo(5);
        Assertions.assertThat(result.getData().getPage()).isEqualTo(0);
        Assertions.assertThat(result.getData().isLast()).isFalse();
        Assertions.assertThat(result.getData().getTotalPages()).isEqualTo(2);
        Assertions.assertThat(result.getData().getTotalElements()).isEqualTo(10);
    }

    @Test
    void clearProductGetAllCache() {
        productService.clearProductGetAllCache();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

}