package org.gkk.bioshopapp.service.service;

import org.gkk.bioshopapp.data.model.Product;
import org.gkk.bioshopapp.service.model.product.*;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductService {

    void create(ProductCreateServiceModel serviceModel, String username);

    void editProduct(String productId, ProductEditServiceModel productEditServiceModel, String username);

    void deleteProduct(String productId, String username);

    Product getProduct(String productId);

    ProductEditServiceModel getProductEditModelById(String productId);

    ProductDeleteServiceModel getProductDeleteModelById(String productId);

    ProductDetailsServiceModel getProductDetailsModel(String productId);

    List<ProductTableServiceModel> getProductTable();

    List<ProductDiscountTableServiceModel> getDiscountedProducts(LocalDateTime localDateTime);

    ProductDetailsServiceModel parseToProductDetailsModel(Product product);

    void initProducts(List<ProductCreateServiceModel> products, String username);
}
