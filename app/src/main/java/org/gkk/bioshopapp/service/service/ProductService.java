package org.gkk.bioshopapp.service.service;

import org.gkk.bioshopapp.data.model.Product;
import org.gkk.bioshopapp.service.model.CreateProductServiceModel;
import org.gkk.bioshopapp.service.model.ProductServiceModel;

import java.util.List;

public interface ProductService {

    void create(CreateProductServiceModel serviceModel);

    List<ProductServiceModel> getAllProducts();

    ProductServiceModel getProductById(String id) throws Exception;

    ProductServiceModel editProduct(String id, CreateProductServiceModel map) throws Exception;

    void deleteProduct(String id);

    Product getProduct(String id) throws Exception;
}
