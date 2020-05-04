package org.gkk.bioshopapp.service.service.impl;

import org.gkk.bioshopapp.data.model.Category;
import org.gkk.bioshopapp.data.model.PriceDiscount;
import org.gkk.bioshopapp.data.model.PriceHistory;
import org.gkk.bioshopapp.data.model.Product;
import org.gkk.bioshopapp.data.repository.ProductRepository;
import org.gkk.bioshopapp.error.ProductNotFoundException;
import org.gkk.bioshopapp.service.model.log.LogServiceModel;
import org.gkk.bioshopapp.service.model.price.PriceDiscountServiceModel;
import org.gkk.bioshopapp.service.model.product.*;
import org.gkk.bioshopapp.service.service.CategoryService;
import org.gkk.bioshopapp.service.service.LogService;
import org.gkk.bioshopapp.service.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.gkk.bioshopapp.constant.ErrorMessageConstant.PRODUCT_NOT_FOUND;
import static org.gkk.bioshopapp.constant.GlobalLogConstant.PRODUCT_ADDED;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final LogService logService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService, LogService logService, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.logService = logService;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public void create(ProductCreateServiceModel serviceModel, String username) {
        Product product = this.modelMapper.map(serviceModel, Product.class);

        Category category = this.categoryService.getCategoryByName(serviceModel.getType());

        if (category == null) {
            category = new Category();
            category.setName(serviceModel.getType());
        }

        product.setCategory(category);

        PriceHistory price = new PriceHistory(serviceModel.getPrice(), LocalDateTime.now());
        price.setProduct(product);

        product.getPrices().add(price);

        this.productRepository.saveAndFlush(product);

        LogServiceModel log = new LogServiceModel(username, PRODUCT_ADDED, product.getId(), LocalDateTime.now());

        this.logService.seedLogInDb(log);
    }

    @Override
    public void editProduct(String id, ProductEditServiceModel model) {
        Product product = this.productRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));

        product.setName(model.getName());
        product.setDescription(model.getDescription());
        product.setImgUrl(model.getImgUrl());

        List<PriceHistory> prices = product.getPrices();

        if (!product.getLastAssignedAmountFromHistory().getPrice().equals(model.getPrice())) {
            product.getLastAssignedAmountFromHistory().setToDate(LocalDateTime.now());

            PriceHistory price = new PriceHistory(model.getPrice(), LocalDateTime.now());
            price.setProduct(product);
            product.getPrices().add(price);
        }

        this.productRepository.saveAndFlush(product);
    }

    @Override
    public void deleteProduct(String id) {
        this.productRepository.setProductDeletedTrue(id);
    }

    @Override
    public Product getProduct(String id) {
        return this.productRepository
                .findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));
    }

    @Override
    public ProductEditServiceModel getProductEditModelById(String id) {
        Product product = this.productRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));

        ProductEditServiceModel productEditServiceModel = this.modelMapper.map(product, ProductEditServiceModel.class);

        BigDecimal regularPrice = product.getLastAssignedAmountFromHistory().getPrice();

        productEditServiceModel.setPrice(regularPrice);

        return productEditServiceModel;
    }

    @Override
    public ProductDetailsServiceModel getProductDetailsModel(String id) {
        Product product = this.productRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));

        ProductDetailsServiceModel productServiceModel = this.modelMapper.map(product, ProductDetailsServiceModel.class);

        PriceHistory lastPrice = product.getLastAssignedAmountFromHistory();
        productServiceModel.setPrice(lastPrice.getPrice());

        setPromotionalPrice(lastPrice, productServiceModel);

        return productServiceModel;
    }

    @Override
    public List<ProductTableServiceModel> getProductTable() {
        return this.productRepository.findAllByDeletedIsFalse().stream()
                .map(p -> {
                    ProductTableServiceModel product = this.modelMapper.map(p, ProductTableServiceModel.class);

                    PriceHistory price = p.getLastAssignedAmountFromHistory();
                    product.setPrice(price.getPrice());

                    setPromotionalPrice(price, product);

                    return product;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDiscountTableServiceModel> getDiscountedProducts(LocalDateTime dateTime) {
        return this.productRepository.findAllInPromotion(dateTime).stream()
                .map(p -> {
                    ProductDiscountTableServiceModel product = this.modelMapper.map(p, ProductDiscountTableServiceModel.class);

                    PriceHistory price = p.getLastAssignedAmountFromHistory();
                    product.setPrice(price.getPrice());

                    PriceDiscountServiceModel lastPromotion =
                            this.modelMapper.map(price.getLastPromotion(), PriceDiscountServiceModel.class);

                    product.setPriceDiscount(lastPromotion);

                    BigDecimal promotionalPrice = price.getPrice().multiply(BigDecimal.valueOf(1 - lastPromotion.getDiscount() / 100.0));
                    product.setPromotionalPrice(promotionalPrice.setScale(2, RoundingMode.HALF_UP));

                    return product;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ProductShoppingCartServiceModel getShoppingCartProductModelById(String id) {
        Product product = this.productRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));

        ProductShoppingCartServiceModel productServiceModel = this.modelMapper.map(product, ProductShoppingCartServiceModel.class);

        PriceHistory lastPrice = product.getLastAssignedAmountFromHistory();
        productServiceModel.setPrice(lastPrice.getPrice());

        setPromotionalPrice(lastPrice, productServiceModel);

        return productServiceModel;
    }

    private void setPromotionalPrice(PriceHistory lastPrice, PricePromotion productServiceModel) {
        if (!lastPrice.getPriceDiscountList().isEmpty()) {
            PriceDiscount lastPromotion = lastPrice.getLastPromotion();

            if ((lastPromotion.getToDate() == null || lastPromotion.getToDate().isAfter(LocalDateTime.now()))
                    && lastPromotion.getFromDate().isBefore(LocalDateTime.now())) {
                BigDecimal promotionalPrice = lastPrice.getPrice().multiply(BigDecimal.valueOf(1 - lastPromotion.getDiscount() / 100.0));
                productServiceModel.setPromotionalPrice(promotionalPrice.setScale(2, RoundingMode.HALF_UP));
            }
        }
    }
}
