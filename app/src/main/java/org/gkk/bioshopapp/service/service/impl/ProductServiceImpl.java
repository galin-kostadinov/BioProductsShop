package org.gkk.bioshopapp.service.service.impl;

import org.gkk.bioshopapp.data.model.Product;
import org.gkk.bioshopapp.data.model.Promotion;
import org.gkk.bioshopapp.data.repository.ProductRepository;
import org.gkk.bioshopapp.service.model.CreateProductServiceModel;
import org.gkk.bioshopapp.service.model.ProductPromoteServiceModel;
import org.gkk.bioshopapp.service.model.ProductServiceModel;
import org.gkk.bioshopapp.service.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void create(CreateProductServiceModel serviceModel) {
        Product product = this.modelMapper.map(serviceModel, Product.class);

        this.productRepository.save(product);
    }

    @Override
    public List<ProductServiceModel> getAllProducts() {
        return this.productRepository.findAll()
                .stream()
                .map(product -> this.modelMapper.map(product, ProductServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProductServiceModel getProductById(String id) throws Exception {
        return this.productRepository
                .findById(id)
                .map(product -> this.modelMapper.map(product, ProductServiceModel.class))
                .orElseThrow(() -> new Exception("No such product in DB"));
    }

    @Override
    public ProductServiceModel editProduct(String id, CreateProductServiceModel model) throws Exception {
        Product product = this.productRepository.findById(id)
                .orElseThrow(() -> new Exception("No such product in DB"));

        product.setName(model.getName());
        product.setDescription(model.getDescription());
        product.setImgUrl(model.getImgUrl());
        product.setMade(model.getMade());
        product.setPrice(model.getPrice());
        product.setType(model.getType());

        Product updatedProduct = this.productRepository.saveAndFlush(product);
        return this.modelMapper.map(updatedProduct, ProductServiceModel.class);
    }

    @Override
    public void deleteProduct(String id) {
        this.productRepository.deleteById(id);
    }

    @Override
    public Product getProduct(String id) throws Exception {
        return this.productRepository
                .findById(id)
                .orElseThrow(() -> new Exception("Product not found"));
    }

    @Override
    public void promote(String productId, Integer discount) throws Exception {
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new Exception("Product not found"));

        Promotion promotion = new Promotion(product, discount);
        product.setPromotion(promotion);

        this.productRepository.saveAndFlush(product);
    }

    @Override
    public List<ProductPromoteServiceModel> getAllPromotedProducts() {
        return this.productRepository.findAllByPromotionNotNull()
                .stream()
                .map(p -> {
                    ProductPromoteServiceModel product = this.modelMapper.map(p, ProductPromoteServiceModel.class);
                    BigDecimal price = p.getPrice().multiply(BigDecimal.valueOf(1 - p.getPromotion().getDiscount() / 100.0));
                    product.setPriceNow(price.setScale(2, RoundingMode.HALF_UP));
                    return product;
                })
                .collect(Collectors.toList()
                );
    }
}
