package org.gkk.bioshopapp.web.controller;

import org.gkk.bioshopapp.service.model.product.ProductDiscountTableServiceModel;
import org.gkk.bioshopapp.service.service.ProductService;
import org.gkk.bioshopapp.web.annotation.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class HomeController extends BaseController {
    private final ProductService productService;

    @Autowired
    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Index")
    public ModelAndView index(ModelAndView model, Principal principal) {

        if (principal != null) {
            return super.redirect("/home");
        }

        setPromotedProducts(model);

        return super.view("index", model);
    }

    @GetMapping("/home")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Home")
    public ModelAndView home(ModelAndView model) {
        setPromotedProducts(model);

        return super.view("home", model);
    }

    @GetMapping("/contacts")
    @PageTitle("Contacts")
    public ModelAndView contact() {
        return super.view("contacts");
    }

    private void setPromotedProducts(ModelAndView model) {
        List<ProductDiscountTableServiceModel> products = this.productService.getDiscountedProducts(LocalDateTime.now());

        if (!products.isEmpty()) {
            model.addObject("promotedProducts", products);
        }
    }
}
