package org.gkk.bioshopapp.init;

import org.gkk.bioshopapp.service.service.CategoryService;
import org.gkk.bioshopapp.service.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInit implements CommandLineRunner {
    private final CategoryService categoryService;
    private final RoleService roleService;

    @Autowired
    public DataInit(CategoryService categoryService, RoleService roleService) {
        this.categoryService = categoryService;
        this.roleService = roleService;
    }

    @Override
    public void run(String... args) throws Exception {
        this.categoryService.initCategories();
        this.roleService.seedRolesInDb();
    }
}
