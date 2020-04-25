package org.gkk.bioshopapp.web.controller;

import org.gkk.bioshopapp.service.model.ProductPromoteServiceModel;
import org.gkk.bioshopapp.service.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class HomeController extends BaseController {
    private final ProductService productService;

    public HomeController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/")
    public ModelAndView index(ModelAndView model) {
        List<ProductPromoteServiceModel> products = this.productService.getAllPromotedProducts();

        if (!products.isEmpty()){
            model.addObject("promotedProducts", products);
        }

        return super.view("index", model);
    }

    @GetMapping("/home")
    public ModelAndView home(ModelAndView model) {
        List<ProductPromoteServiceModel> products = this.productService.getAllPromotedProducts();

        if (!products.isEmpty()){
            model.addObject("promotedProducts", products);
        }

        return super.view("home", model);
    }

    @GetMapping("/contacts")
    public ModelAndView contact() {
        return super.view("contacts");
    }
}
