package org.gkk.bioshopapp.service.service.impl;

import org.gkk.bioshopapp.constant.GlobalLogConstant;
import org.gkk.bioshopapp.data.model.*;
import org.gkk.bioshopapp.data.repository.ProductRepository;
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

import static org.gkk.bioshopapp.constant.GlobalLogConstant.*;

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
        Product product = this.productRepository.findByIdAndDeletedIsFalse(id);

        product.setName(model.getName());
        product.setDescription(model.getDescription());
        product.setImgUrl(model.getImgUrl());

        List<PriceHistory> prices = product.getPrices();

        if (!getLastPriceFromPriceHistory(product.getPrices()).getPrice().equals(model.getPrice())) {
            getLastPriceFromPriceHistory(product.getPrices()).setToDate(LocalDateTime.now());

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
                .findByIdAndDeletedIsFalse(id);
    }

    @Override
    public ProductEditServiceModel getProductEditModelById(String id) {
        Product product = this.productRepository.findByIdAndDeletedIsFalse(id);

        ProductEditServiceModel productEditServiceModel = this.modelMapper.map(product, ProductEditServiceModel.class);

        BigDecimal regularPrice = getLastPriceFromPriceHistory(product.getPrices()).getPrice();

        productEditServiceModel.setPrice(regularPrice);

        return productEditServiceModel;
    }

    @Override
    public ProductDetailsServiceModel getProductDetailsModel(String id) {
        Product product = this.productRepository.findByIdAndDeletedIsFalse(id);

        ProductDetailsServiceModel productServiceModel = this.modelMapper.map(product, ProductDetailsServiceModel.class);

        PriceHistory lastPrice = getLastPriceFromPriceHistory(product.getPrices());
        productServiceModel.setPrice(lastPrice.getPrice());

        setPromotionalPrice(lastPrice, productServiceModel);

        return productServiceModel;
    }

    @Override
    public List<ProductTableServiceModel> getProductTable() {
        return this.productRepository.findAllByDeletedIsFalse().stream()
                .map(p -> {
                    ProductTableServiceModel product = this.modelMapper.map(p, ProductTableServiceModel.class);

                    PriceHistory price = getLastPriceFromPriceHistory(p.getPrices());
                    product.setPrice(price.getPrice());

                    setPromotionalPrice(price, product);

                    return product;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDiscountTableServiceModel> getDiscountedProducts() {
        return this.productRepository.findAllInPromotion(LocalDateTime.now()).stream()
                .map(p -> {
                    ProductDiscountTableServiceModel product = this.modelMapper.map(p, ProductDiscountTableServiceModel.class);

                    PriceHistory price = getLastPriceFromPriceHistory(p.getPrices());
                    product.setPrice(price.getPrice());

                    PriceDiscountServiceModel lastPromotion =
                            this.modelMapper.map(getLastPricePromotion(price.getPriceDiscountList()),
                                    PriceDiscountServiceModel.class);

                    product.setPriceDiscount(lastPromotion);

                    BigDecimal promotionalPrice = price.getPrice().multiply(BigDecimal.valueOf(1 - lastPromotion.getDiscount() / 100.0));
                    product.setPromotionalPrice(promotionalPrice.setScale(2, RoundingMode.HALF_UP));

                    return product;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ProductShoppingCartServiceModel getShoppingCartProductModelById(String id) {
        Product product = this.productRepository.findByIdAndDeletedIsFalse(id);

        ProductShoppingCartServiceModel productServiceModel = this.modelMapper.map(product, ProductShoppingCartServiceModel.class);

        PriceHistory lastPrice = getLastPriceFromPriceHistory(product.getPrices());
        productServiceModel.setPrice(lastPrice.getPrice());

        setPromotionalPrice(lastPrice, productServiceModel);

        return productServiceModel;
    }

    private void setPromotionalPrice(PriceHistory lastPrice, PricePromotion productServiceModel) {
        if (!lastPrice.getPriceDiscountList().isEmpty()) {
            PriceDiscount lastPromotion = getLastPricePromotion(lastPrice.getPriceDiscountList());

            if ((lastPromotion.getToDate() == null || lastPromotion.getToDate().isAfter(LocalDateTime.now()))
                    && lastPromotion.getFromDate().isBefore(LocalDateTime.now())) {
                BigDecimal promotionalPrice = lastPrice.getPrice().multiply(BigDecimal.valueOf(1 - lastPromotion.getDiscount() / 100.0));
                productServiceModel.setPromotionalPrice(promotionalPrice.setScale(2, RoundingMode.HALF_UP));
            }
        }
    }

    private PriceHistory getLastPriceFromPriceHistory(List<PriceHistory> prices) {
        return prices.stream()
                .min((p1, p2) -> p2.getFromDate().compareTo(p1.getFromDate()))
                .orElseThrow();
    }

    private PriceDiscount getLastPricePromotion(List<PriceDiscount> priceDiscountList) {
        return priceDiscountList.stream()
                .min((pd1, pd2) -> pd2.getFromDate().compareTo(pd1.getFromDate()))
                .orElseThrow();
    }
}
