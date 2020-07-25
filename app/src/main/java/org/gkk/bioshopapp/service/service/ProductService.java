package org.gkk.bioshopapp.service.service;

import org.gkk.bioshopapp.data.model.Product;
import org.gkk.bioshopapp.service.model.product.*;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductService {

    void create(ProductCreateServiceModel serviceModel, String username);

    void editProduct(String id, ProductEditServiceModel productEditServiceModel, String username);

    void deleteProduct(String id, String username);

    Product getProduct(String id);

    ProductEditServiceModel getProductEditModelById(String id);

    ProductDeleteServiceModel getProductDeleteModelById(String id);

    ProductDetailsServiceModel getProductDetailsModel(String id);

    List<ProductTableServiceModel> getProductTable();

    List<ProductDiscountTableServiceModel> getDiscountedProducts(LocalDateTime localDateTime);

    ProductDetailsServiceModel parseToProductDetailsModel(Product product);

    void initProducts(List<ProductCreateServiceModel> products, String username);
}
