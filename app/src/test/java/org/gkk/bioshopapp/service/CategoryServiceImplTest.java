package org.gkk.bioshopapp.service;

import org.gkk.bioshopapp.base.TestBase;
import org.gkk.bioshopapp.data.model.Category;
import org.gkk.bioshopapp.data.model.ProductType;
import org.gkk.bioshopapp.data.repository.CategoryRepository;
import org.gkk.bioshopapp.error.CategoryNotFoundException;
import org.gkk.bioshopapp.service.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CategoryServiceImplTest extends TestBase {

    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Test
    public void getCategoryByName_whenNotExist_ShouldThrowException() {
        ProductType productType = ProductType.FRUIT;
        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryByName(productType));
    }

    @Test
    public void getCategoryByName_whenExist_ShouldReturnCategory() {
        ProductType productType = ProductType.FRUIT;
        Category category = new Category();
        category.setName(productType);
        category.setId(UUID.randomUUID().toString());

        Mockito.when(categoryRepository.findByName(productType)).thenReturn(Optional.of(category));

        Category categoryDB = categoryService.getCategoryByName(productType);

        assertNotNull(categoryDB);
        assertEquals(category.getName(), categoryDB.getName());
        assertEquals(category.getId(), categoryDB.getId());
    }
}