package org.gkk.bioshopapp.service.service.impl;

import org.gkk.bioshopapp.data.model.*;
import org.gkk.bioshopapp.data.repository.ProductRepository;
import org.gkk.bioshopapp.error.ProductNotFoundException;
import org.gkk.bioshopapp.service.model.log.LogServiceModel;
import org.gkk.bioshopapp.service.model.product.*;
import org.gkk.bioshopapp.service.service.CategoryService;
import org.gkk.bioshopapp.service.service.LogService;
import org.gkk.bioshopapp.service.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceImplTest {
    ProductRepository productRepository;
    CategoryService categoryService;
    LogService logService;
    ModelMapper modelMapper;

    ProductService productService;

    @BeforeEach
    public void setupTest() {
        productRepository = Mockito.mock(ProductRepository.class);
        categoryService = Mockito.mock(CategoryService.class);
        logService = Mockito.mock(LogService.class);
        modelMapper = new ModelMapper();

        productService = new ProductServiceImpl(productRepository, categoryService, logService, modelMapper);
    }

    @Test
    public void create_whenCategoryNotExist_shouldCreateProductWithNewCategoryAndLog() {
        BigDecimal price = BigDecimal.ONE;
        ProductType type = ProductType.FRUIT;
        String username = "ivanov";

        ProductCreateServiceModel serviceModel = new ProductCreateServiceModel();
        serviceModel.setPrice(price);
        serviceModel.setType(type);

        productService.create(serviceModel, username);

        ArgumentCaptor<Product> argumentProduct = ArgumentCaptor.forClass(Product.class);
        Mockito.verify(productRepository).saveAndFlush(argumentProduct.capture());

        Product product = argumentProduct.getValue();

        ArgumentCaptor<LogServiceModel> argumentLog = ArgumentCaptor.forClass(LogServiceModel.class);
        Mockito.verify(logService).seedLogInDb(argumentLog.capture());

        LogServiceModel log = argumentLog.getValue();

        assertEquals(price, product.getPrices().get(0).getPrice());
        assertEquals(type, product.getCategory().getName());
        assertNotNull(log);
    }

    @Test
    public void create_whenCategoryNotExist_shouldCreateProductWithExistingCategoryAndLog() {
        BigDecimal price = BigDecimal.ONE;
        ProductType type = ProductType.FRUIT;
        String username = "ivanov";
        String categoryId = "ivanov123";

        ProductCreateServiceModel serviceModel = new ProductCreateServiceModel();
        serviceModel.setPrice(price);
        serviceModel.setType(type);

        Category category = new Category();
        category.setName(type);
        category.setId(categoryId);

        Mockito.when(categoryService.getCategoryByName(serviceModel.getType())).thenReturn(category);

        productService.create(serviceModel, username);

        ArgumentCaptor<Product> argumentProduct = ArgumentCaptor.forClass(Product.class);
        Mockito.verify(productRepository).saveAndFlush(argumentProduct.capture());

        Product product = argumentProduct.getValue();

        ArgumentCaptor<LogServiceModel> argumentLog = ArgumentCaptor.forClass(LogServiceModel.class);
        Mockito.verify(logService).seedLogInDb(argumentLog.capture());

        LogServiceModel log = argumentLog.getValue();

        assertEquals(price, product.getPrices().get(0).getPrice());
        assertEquals(type, product.getCategory().getName());
        assertEquals(categoryId, product.getCategory().getId());
        assertNotNull(log);
    }

    @Test
    public void editProduct_whenProductNotExist_shouldThrowException() {
        String id = "123abc";
        ProductEditServiceModel productModel = new ProductEditServiceModel();

        assertThrows(ProductNotFoundException.class, () -> productService.editProduct(id, productModel));
    }

    @Test
    public void editProduct_whenProductExist_shouldThrowException() {
        String id = "123abc";
        ProductEditServiceModel productModel = new ProductEditServiceModel();

        assertThrows(ProductNotFoundException.class, () -> productService.editProduct(id, productModel));
    }

    @Test
    public void editProduct_whenProductNameIsDifferent_shouldSetNewName() {
        String id = "123abc";
        String oldProductName = "coconut_coconut";
        String newProductName = "coconut";
        BigDecimal price = BigDecimal.ONE;

        List<PriceHistory> prices = new ArrayList<>();
        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setPrice(price);
        prices.add(priceHistory);

        ProductEditServiceModel productModel = new ProductEditServiceModel();
        productModel.setName(newProductName);
        productModel.setPrice(price);

        Product product = new Product();
        product.setId(id);
        product.setName(oldProductName);
        product.setPrices(prices);

        Mockito.when(productRepository.findByIdAndDeletedIsFalse(id)).thenReturn(Optional.of(product));

        productService.editProduct(id, productModel);
        assertEquals(newProductName, product.getName());
    }

    @Test
    public void editProduct_whenImgUrlIsDifferent_shouldSetNewImgUrl() {
        String id = "123abc";
        String oldUrl = "www.bio.com/coconut_coconut";
        String newUrl = "www.bio.com/coconut";
        BigDecimal price = BigDecimal.ONE;

        List<PriceHistory> prices = new ArrayList<>();
        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setPrice(price);
        prices.add(priceHistory);

        ProductEditServiceModel productModel = new ProductEditServiceModel();
        productModel.setImgUrl(newUrl);
        productModel.setPrice(price);

        Product product = new Product();
        product.setId(id);
        product.setImgUrl(oldUrl);
        product.setPrices(prices);

        Mockito.when(productRepository.findByIdAndDeletedIsFalse(id)).thenReturn(Optional.of(product));

        productService.editProduct(id, productModel);
        assertEquals(newUrl, product.getImgUrl());
    }

    @Test
    public void editProduct_whenProductPriceIsDifferent_shouldUpdatePriceHistory() {
        String id = "123abc";
        BigDecimal oldPrice = BigDecimal.ONE;
        BigDecimal newPrice = BigDecimal.TEN;

        List<PriceHistory> prices = new ArrayList<>();
        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setPrice(oldPrice);
        priceHistory.setFromDate(LocalDateTime.now().minusHours(1));
        prices.add(priceHistory);

        ProductEditServiceModel productModel = new ProductEditServiceModel();
        productModel.setPrice(newPrice);

        Product product = new Product();
        product.setId(id);
        product.setPrices(prices);

        Mockito.when(productRepository.findByIdAndDeletedIsFalse(id)).thenReturn(Optional.of(product));

        productService.editProduct(id, productModel);

        assertEquals(newPrice, product.getLastAssignedAmountFromHistory().getPrice());
    }

    @Test
    public void getProduct_whenProductNotExist_shouldThrowException() {
        String id = "123abc";

        assertThrows(ProductNotFoundException.class, () -> productService.getProduct(id));
    }

    @Test
    public void getProduct_whenProductExist_shouldReturnProduct() {
        String id = "123abc";
        Product product = new Product();
        product.setId(id);

        Mockito.when(productRepository.findByIdAndDeletedIsFalse(id)).thenReturn(Optional.of(product));

        assertEquals(product, productService.getProduct(id));
    }

    @Test
    public void getProductEditModelById_whenProductNotExist_shouldThrowException() {
        String id = "123abc";

        assertThrows(ProductNotFoundException.class, () -> productService.getProductEditModelById(id));
    }

    @Test
    public void getProductEditModelById_whenProductExist_shouldReturnProductModel() {
        String id = "123abc";
        BigDecimal price = BigDecimal.ONE;
        List<PriceHistory> prices = new ArrayList<>();
        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setPrice(price);
        prices.add(priceHistory);

        Product product = new Product();
        product.setId(id);
        product.setPrices(prices);

        Mockito.when(productRepository.findByIdAndDeletedIsFalse(id)).thenReturn(Optional.of(product));

        ProductEditServiceModel productModel = productService.getProductEditModelById(id);

        assertNotNull(productModel);
        assertEquals(id, productModel.getId());
        assertEquals(price, productModel.getPrice());
    }

    @Test
    public void getProductDetailsModel_whenProductNotExist_shouldThrowException() {
        String id = "123abc";

        assertThrows(ProductNotFoundException.class, () -> productService.getProductDetailsModel(id));
    }

    @Test
    public void getProductDetailsModel_whenProductExist_shouldReturnProductModel() {
        String id = "123abc";
        BigDecimal price = BigDecimal.ONE;
        List<PriceHistory> prices = new ArrayList<>();
        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setPrice(price);
        prices.add(priceHistory);

        Product product = new Product();
        product.setId(id);
        product.setPrices(prices);

        Mockito.when(productRepository.findByIdAndDeletedIsFalse(id)).thenReturn(Optional.of(product));

        ProductDetailsServiceModel productModel = productService.getProductDetailsModel(id);

        assertNotNull(productModel);
        assertEquals(id, productModel.getId());
        assertEquals(price, productModel.getPrice());
    }

    @Test
    public void getProductTable_whenProductRepositoryIsEmpty_shouldReturnEmptyList() {
        Mockito.when(productRepository.findAllByDeletedIsFalse()).thenReturn(new ArrayList<>());

        assertTrue(productService.getProductTable().isEmpty());
    }

    @Test
    public void getProductTable_whenProductRepositoryIsNotEmpty_shouldReturnModelList() {
        String id = "123abc";
        BigDecimal price = BigDecimal.ONE;
        List<PriceHistory> prices = new ArrayList<>();
        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setPrice(price);
        prices.add(priceHistory);

        Product product = new Product();
        product.setId(id);
        product.setPrices(prices);

        List<Product> products = new ArrayList<>();
        products.add(product);

        Mockito.when(productRepository.findAllByDeletedIsFalse()).thenReturn(products);

        List<ProductTableServiceModel> productsModel = productService.getProductTable();

        assertNotNull(productsModel.get(0));
        assertEquals(id, productsModel.get(0).getId());
        assertEquals(price, productsModel.get(0).getPrice());
    }

    @Test
    public void getDiscountedProducts_whenProductNotHavePromotion_shouldReturnEmptyList() {
        Mockito.when(productRepository.findAllInPromotion(LocalDateTime.now())).thenReturn(new ArrayList<>());

        assertTrue(productService.getDiscountedProducts(LocalDateTime.now()).isEmpty());
    }

    @Test
    public void getDiscountedProducts_whenProductRepositoryIsNotEmpty_shouldReturnModelList() {
        String id = "123abc";
        BigDecimal price = BigDecimal.TEN;
        Integer discount = 10;
        List<PriceHistory> prices = new ArrayList<>();
        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setPrice(price);
        priceHistory.setFromDate(LocalDateTime.now().minusHours(2));

        PriceDiscount priceDiscount = new PriceDiscount();
        priceDiscount.setFromDate(LocalDateTime.now().minusHours(1));
        priceDiscount.setToDate(LocalDateTime.now().plusHours(2));
        priceDiscount.setDiscount(discount);
        priceDiscount.setPrice(priceHistory);

        List<PriceDiscount> priceDiscounts = new ArrayList<>();
        priceDiscounts.add(priceDiscount);

        priceHistory.setPriceDiscountList(priceDiscounts);

        prices.add(priceHistory);

        Product product = new Product();
        product.setId(id);
        product.setPrices(prices);

        List<Product> products = new ArrayList<>();
        products.add(product);

        LocalDateTime dateTime = LocalDateTime.now();

        Mockito.when(productRepository.findAllInPromotion(dateTime)).thenReturn(products);

        List<ProductDiscountTableServiceModel> productsModel = productService.getDiscountedProducts(dateTime);

        assertNotNull(productsModel.get(0));
        assertEquals(id, productsModel.get(0).getId());
        assertEquals(price, productsModel.get(0).getPrice());

        BigDecimal expectedPromotionalPrice = price.subtract(BigDecimal.ONE).setScale(2, RoundingMode.HALF_UP);
        assertEquals(expectedPromotionalPrice, productsModel.get(0).getPromotionalPrice());
    }

    @Test
    public void getShoppingCartProductModelById_whenProductNotExist_shouldThrowException() {
        String id = "123abc";

        assertThrows(ProductNotFoundException.class, () -> productService.getProductDetailsModel(id));
    }

    @Test
    public void getShoppingCartProductModelById_whenProductExist_shouldReturnProductModel() {
        String id = "123abc";
        BigDecimal price = BigDecimal.ONE;
        List<PriceHistory> prices = new ArrayList<>();
        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setPrice(price);
        prices.add(priceHistory);

        Product product = new Product();
        product.setId(id);
        product.setPrices(prices);

        Mockito.when(productRepository.findByIdAndDeletedIsFalse(id)).thenReturn(Optional.of(product));

        ProductShoppingCartServiceModel productModel = productService.getShoppingCartProductModelById(id);

        assertNotNull(productModel);
        assertEquals(id, productModel.getId());
        assertEquals(price, productModel.getPrice());
    }

    @Test
    public void getShoppingCartProductModelById_whenProductIsInPromotion_shouldReturnModelWithPromotionPrice() {
        String id = "123abc";
        BigDecimal price = BigDecimal.TEN;
        Integer discount = 10;
        List<PriceHistory> prices = new ArrayList<>();
        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setPrice(price);
        priceHistory.setFromDate(LocalDateTime.now().minusHours(2));

        PriceDiscount priceDiscount = new PriceDiscount();
        priceDiscount.setFromDate(LocalDateTime.now().minusHours(1));
        priceDiscount.setToDate(LocalDateTime.now().plusHours(2));
        priceDiscount.setDiscount(discount);
        priceDiscount.setPrice(priceHistory);

        List<PriceDiscount> priceDiscounts = new ArrayList<>();
        priceDiscounts.add(priceDiscount);

        priceHistory.setPriceDiscountList(priceDiscounts);

        prices.add(priceHistory);

        Product product = new Product();
        product.setId(id);
        product.setPrices(prices);

        Mockito.when(productRepository.findByIdAndDeletedIsFalse(id)).thenReturn(Optional.of(product));

        ProductShoppingCartServiceModel productsModel = productService.getShoppingCartProductModelById(id);

        assertNotNull(productsModel);
        assertEquals(id, productsModel.getId());
        assertEquals(price, productsModel.getPrice());

        BigDecimal expectedPromotionalPrice = price.subtract(BigDecimal.ONE).setScale(2, RoundingMode.HALF_UP);
        assertEquals(expectedPromotionalPrice, productsModel.getPromotionalPrice());
    }
}