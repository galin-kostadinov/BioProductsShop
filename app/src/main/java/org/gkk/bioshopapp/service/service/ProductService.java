package org.gkk.bioshopapp.service.service;

import org.gkk.bioshopapp.data.model.Product;
import org.gkk.bioshopapp.service.model.product.*;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductService {

    void create(ProductCreateServiceModel serviceModel, String username);

    void editProduct(String id, ProductEditServiceModel map);

    void deleteProduct(String id);

    Product getProduct(String id);

    ProductEditServiceModel getProductEditModelById(String id);

    ProductDetailsServiceModel getProductDetailsModel(String id) ;

    List<ProductTableServiceModel> getProductTable();

    List<ProductDiscountTableServiceModel> getDiscountedProducts(LocalDateTime localDateTime);

    ProductShoppingCartServiceModel getShoppingCartProductModelById(String id);
}
