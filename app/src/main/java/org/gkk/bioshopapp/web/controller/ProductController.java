package org.gkk.bioshopapp.web.controller;

import org.gkk.bioshopapp.service.model.price.PriceDiscountServiceModel;
import org.gkk.bioshopapp.service.model.product.*;
import org.gkk.bioshopapp.service.service.PriceDiscountService;
import org.gkk.bioshopapp.service.service.PriceHistoryService;
import org.gkk.bioshopapp.service.service.ProductService;
import org.gkk.bioshopapp.web.model.product.PriceDiscountModel;
import org.gkk.bioshopapp.web.model.product.ProductCreateModel;
import org.gkk.bioshopapp.web.model.product.ProductDetailsModel;
import org.gkk.bioshopapp.web.model.product.ProductEditModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController extends BaseController {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    private final ProductService productService;
    private final PriceHistoryService priceHistoryService;
    private final PriceDiscountService priceDiscountService;
    private final ModelMapper modelMapper;

    public ProductController(ProductService productService, PriceHistoryService priceHistoryService, PriceDiscountService priceDiscountService, ModelMapper modelMapper) {
        this.productService = productService;
        this.priceHistoryService = priceHistoryService;
        this.priceDiscountService = priceDiscountService;
        this.modelMapper = modelMapper;
    }

    @GetMapping({"/", ""})
    public ModelAndView getAllProducts(ModelAndView model) {
        List<ProductTableServiceModel> products = this.productService.getProductTable();

        model.addObject("products", products);

        return super.view("product/products", model);
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

        ProductCreateServiceModel serviceModel = this.modelMapper.map(model, ProductCreateServiceModel.class);

        try {
            this.productService.create(serviceModel);
            return super.redirect("/product");
        } catch (Exception e) {
            return super.redirect("/product/create-product");
        }
    }

    @GetMapping("/product-table")
    public ModelAndView getProductTable(ModelAndView model) {
        List<ProductTableServiceModel> products = this.productService.getProductTable();

        model.addObject("products", products);

        return super.view("product/product-table", model);
    }

    @GetMapping("/details/{id}")
    public ModelAndView detailsProduct(@PathVariable String id, ModelAndView model) {
        ProductDetailsModel product =
                this.modelMapper.map(this.productService.getProductDetailsModel(id), ProductDetailsModel.class);

        model.addObject("product", product);
        model.addObject("productId", id);

        return super.view("product/details-product", model);
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editProduct(@PathVariable String id, ModelAndView model) {
        ProductEditModel product = this.modelMapper.map(this.productService.getProductEditModelById(id), ProductEditModel.class);

        model.addObject("product", product);
        model.addObject("productId", id);

        return super.view("product/edit-product", model);
    }

    @PostMapping("/edit/{id}")
    public ModelAndView editProductConfirm(@PathVariable String id, @ModelAttribute ProductEditModel model) {
        this.productService.editProduct(id, this.modelMapper.map(model, ProductEditServiceModel.class));

        return super.redirect("/product/details/" + id);
    }


    @GetMapping("/delete/{id}")
    public ModelAndView deleteProduct(@PathVariable String id, ModelAndView model) {
        ProductEditServiceModel product = this.productService.getProductEditModelById(id);

        model.addObject("product", product);
        model.addObject("productId", id);

        return super.view("product/delete-product", model);
    }

    @PostMapping("/delete/{id}")
    public ModelAndView deleteProductConfirm(@PathVariable String id, @ModelAttribute ProductCreateModel model) {
        this.productService.deleteProduct(id);

        return super.redirect("/product/product-table");
    }

    @GetMapping("/promote/{id}")
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
    public ModelAndView promote(@PathVariable String id, @ModelAttribute PriceDiscountModel model) {
        PriceDiscountServiceModel priceDiscountServiceModel = this.modelMapper.map(model, PriceDiscountServiceModel.class);

        priceDiscountServiceModel.setFromDate(LocalDateTime.parse(model.getFromDate(), formatter));
        priceDiscountServiceModel.setToDate(LocalDateTime.parse(model.getToDate(), formatter));

        this.priceHistoryService.setDiscount(id, priceDiscountServiceModel);

        return super.redirect("/product/product-table");
    }

    @GetMapping("/promotion-table")
    public ModelAndView getPromotionalProductTable(ModelAndView model) {
        List<ProductDiscountTableServiceModel> products = this.productService.getDiscountedProducts();

        model.addObject("products", products);

        return super.view("product/promotion-table", model);
    }

    @PostMapping("/remove-promotion/{id}")
    public ModelAndView removePromotion(@PathVariable String id) {
        this.priceDiscountService.removePromotion(id);

        return super.redirect("/product/promotion-table");
    }
}
