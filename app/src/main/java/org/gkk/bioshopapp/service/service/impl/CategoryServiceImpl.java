package org.gkk.bioshopapp.service.service.impl;

import org.gkk.bioshopapp.data.model.Category;
import org.gkk.bioshopapp.data.model.ProductType;
import org.gkk.bioshopapp.data.repository.CategoryRepository;
import org.gkk.bioshopapp.service.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category getCategoryByName(ProductType name) {
        return this.categoryRepository.findByName(name);
    }
}
