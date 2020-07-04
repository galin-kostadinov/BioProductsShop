package org.gkk.bioshopapp.web.controller;

import org.gkk.bioshopapp.service.model.price.PriceDiscountServiceModel;
import org.gkk.bioshopapp.service.model.product.*;
import org.gkk.bioshopapp.service.service.PriceDiscountService;
import org.gkk.bioshopapp.service.service.PriceHistoryService;
import org.gkk.bioshopapp.service.service.ProductService;
import org.gkk.bioshopapp.web.annotation.PageTitle;
import org.gkk.bioshopapp.web.model.product.PriceDiscountModel;
import org.gkk.bioshopapp.web.model.product.ProductCreateBindingModel;
import org.gkk.bioshopapp.web.model.product.ProductDetailsModel;
import org.gkk.bioshopapp.web.model.product.ProductEditModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController extends BaseController {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    private final ProductService productService;
    private final PriceHistoryService priceHistoryService;
    private final PriceDiscountService priceDiscountService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductController(ProductService productService, PriceHistoryService priceHistoryService, PriceDiscountService priceDiscountService, ModelMapper modelMapper) {
        this.productService = productService;
        this.priceHistoryService = priceHistoryService;
        this.priceDiscountService = priceDiscountService;
        this.modelMapper = modelMapper;
    }

    @GetMapping({"/", ""})
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Products")
    public ModelAndView getAllProducts(ModelAndView model) {
        List<ProductTableServiceModel> products = this.productService.getProductTable();

        model.addObject("products", products);

        return super.view("product/products", model);
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Create Product")
    public String getCreateForm(Model model) {
        if (model.getAttribute("productCreateBindingModel") == null) {
            model.addAttribute("productCreateBindingModel", new ProductCreateBindingModel());
        }

        return "product/create-product";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String createConfirm(@Valid @ModelAttribute ProductCreateBindingModel productCreateBindingModel,
                                BindingResult bindingResult, RedirectAttributes redirectAttributes, Principal principal) {

        if (productCreateBindingModel == null || bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("productCreateBindingModel", productCreateBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.productCreateBindingModel", bindingResult);
            return this.redirectStr("create");
        }

        ProductCreateServiceModel serviceModel = this.modelMapper.map(productCreateBindingModel, ProductCreateServiceModel.class);

        String username = principal.getName();
        this.productService.create(serviceModel, username);

        return this.redirectStr("/products");
    }

    @GetMapping("/product-table")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Product Table")
    public ModelAndView getProductTable(ModelAndView model) {
        List<ProductTableServiceModel> products = this.productService.getProductTable();

        model.addObject("products", products);

        return super.view("product/product-table", model);
    }

    @GetMapping("/details/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Product Details")
    public ModelAndView detailsProduct(@PathVariable String id, ModelAndView model) {
        ProductDetailsModel product =
                this.modelMapper.map(this.productService.getProductDetailsModel(id), ProductDetailsModel.class);

        model.addObject("product", product);
        model.addObject("productId", id);

        return super.view("product/details-product", model);
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Edit Product")
    public ModelAndView editProduct(@PathVariable String id, ModelAndView model) {
        ProductEditModel product = this.modelMapper.map(this.productService.getProductEditModelById(id), ProductEditModel.class);

        model.addObject("product", product);
        model.addObject("productId", id);

        return super.view("product/edit-product", model);
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView editProductConfirm(@PathVariable String id, @ModelAttribute ProductEditModel model) {
        this.productService.editProduct(id, this.modelMapper.map(model, ProductEditServiceModel.class));

        return super.redirect("/product/details/" + id);
    }


    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Delete Product")
    public ModelAndView deleteProduct(@PathVariable String id, ModelAndView model) {
        ProductEditServiceModel product = this.productService.getProductEditModelById(id);

        model.addObject("product", product);
        model.addObject("productId", id);

        return super.view("product/delete-product", model);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteProductConfirm(@PathVariable String id, @ModelAttribute ProductCreateBindingModel model) {
        this.productService.deleteProduct(id);

        return super.redirect("/product/product-table");
    }

    @GetMapping("/promote/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Promote Product")
    public ModelAndView getPromoteForm(@PathVariable String id, ModelAndView model) {
        ProductDetailsServiceModel productServiceModel = this.productService.getProductDetailsModel(id);

        if (productServiceModel.getPromotionalPrice() != null) {
            return super.redirect("/product/product-table");
        }

        ProductDetailsModel product =
                this.modelMapper.map(productServiceModel, ProductDetailsModel.class);

        model.addObject("product", product);
        model.addObject("productId", id);
        model.addObject("dataTimeNow", LocalDateTime.now().format(formatter));

        return super.view("product/promote-form", model);
    }

    @PostMapping("/promote/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView promote(@PathVariable String id, @ModelAttribute PriceDiscountModel model) {
        PriceDiscountServiceModel priceDiscountServiceModel = this.modelMapper.map(model, PriceDiscountServiceModel.class);

        priceDiscountServiceModel.setFromDate(LocalDateTime.parse(model.getFromDate(), formatter));
        priceDiscountServiceModel.setToDate(LocalDateTime.parse(model.getToDate(), formatter));

        this.priceHistoryService.setDiscount(id, priceDiscountServiceModel);

        return super.redirect("/product/product-table");
    }

    @GetMapping("/promotion-table")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Promotional Table")
    public ModelAndView getPromotionalProductTable(ModelAndView model) {
        List<ProductDiscountTableServiceModel> products = this.productService.getDiscountedProducts(LocalDateTime.now());

        model.addObject("products", products);

        return super.view("product/promotion-table", model);
    }

    @PostMapping("/remove-promotion/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView removePromotion(@PathVariable String id) {
        this.priceDiscountService.removePromotion(id);

        return super.redirect("/product/promotion-table");
    }
}
