package org.gkk.bioshopapp.service.service;

import org.gkk.bioshopapp.data.model.Category;
import org.gkk.bioshopapp.data.model.ProductType;

public interface CategoryService {
    Category getCategoryByName(ProductType name);
}
