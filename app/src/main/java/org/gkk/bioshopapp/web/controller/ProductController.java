package org.gkk.bioshopapp.web.controller;

import org.gkk.bioshopapp.service.model.CreateProductServiceModel;
import org.gkk.bioshopapp.service.model.ProductServiceModel;
import org.gkk.bioshopapp.service.service.ProductService;
import org.gkk.bioshopapp.web.model.ProductCreateModel;
import org.gkk.bioshopapp.web.model.ProductDetailsModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController extends BaseController {
    private final ProductService productService;
    private final ModelMapper modelMapper;

    public ProductController(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping({"/", ""})
    ModelAndView getProducts() {
        return super.view("product/products");
    }

    @GetMapping("/create")
    public ModelAndView getCreateForm() {
        ModelAndView modelAndView = super.view("product/create-product");
        modelAndView.addObject("model", new ProductCreateModel());
        return modelAndView;
    }

    @PostMapping("/create")
    ModelAndView create(@ModelAttribute ProductCreateModel model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return super.view("product/create-product");
        }

        CreateProductServiceModel serviceModel = this.modelMapper.map(model, CreateProductServiceModel.class);

        try {
            this.productService.create(serviceModel);
            return super.redirect("/product");
        } catch (Exception e) {
            return super.redirect("/product/create-product");
        }
    }

    @GetMapping("/all")
    public ModelAndView getAllProducts(ModelAndView model) {
        List<ProductServiceModel> products = this.productService.getAllProducts();

        model.addObject("products", products);

        //admin
        //model.setViewName("product/all-products-table");

        //user
        model.setViewName("product/all-products");

        //guest

        return model;
    }

    @GetMapping("/details/{id}")
    public ModelAndView detailsProduct(@PathVariable String id, ModelAndView model) throws Exception {
        ProductDetailsModel product = this.modelMapper.map(this.productService.getProductById(id), ProductDetailsModel.class); ;

        model.addObject("product", product);
        model.addObject("productId", id);

        return super.view("product/details-product", model);
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editProduct(@PathVariable String id, ModelAndView model) throws Exception {
        ProductServiceModel product = this.productService.getProductById(id);

        model.addObject("product", product);
        model.addObject("productId", id);

        return super.view("product/edit-product", model);
    }

    @PostMapping("/edit/{id}")
    public ModelAndView editProductConfirm(@PathVariable String id, @ModelAttribute ProductCreateModel model) throws Exception {
        this.productService.editProduct(id, this.modelMapper.map(model, CreateProductServiceModel.class));

        return super.redirect("/product/details/" + id);
    }


    @GetMapping("/delete/{id}")
    public ModelAndView deleteProduct(@PathVariable String id, ModelAndView model) throws Exception {
        ProductServiceModel product = this.productService.getProductById(id);

        model.addObject("product", product);
        model.addObject("productId", id);

        return super.view("product/delete-product", model);
    }

    @PostMapping("/delete/{id}")
    public ModelAndView deleteProductConfirm(@PathVariable String id, @ModelAttribute ProductCreateModel model) throws Exception {
        this.productService.deleteProduct(id);

        return super.redirect("/product/all");
    }
}
