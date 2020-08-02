package org.gkk.bioshopapp.web.view.controller;

import org.gkk.bioshopapp.data.model.ProductType;
import org.gkk.bioshopapp.service.model.product.ProductDeleteServiceModel;
import org.gkk.bioshopapp.service.model.product.ProductDetailsServiceModel;
import org.gkk.bioshopapp.service.model.product.ProductEditServiceModel;
import org.gkk.bioshopapp.service.service.PriceDiscountService;
import org.gkk.bioshopapp.service.service.PriceHistoryService;
import org.gkk.bioshopapp.service.service.ProductService;
import org.gkk.bioshopapp.service.service.ShoppingCartService;
import org.gkk.bioshopapp.web.view.model.product.PriceDiscountBindingModel;
import org.gkk.bioshopapp.web.view.model.product.ProductCreateBindingModel;
import org.gkk.bioshopapp.web.view.model.product.ProductEditModel;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @MockBean
    private ProductService productService;

    @MockBean
    private  PriceHistoryService priceHistoryService;

    @MockBean
    private PriceDiscountService priceDiscountService;

    @MockBean
    private ShoppingCartService shoppingCartService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void testGetAllProducts() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("product/products"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void testGetCreateForm() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/products/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("product/create-product"))
                .andExpect(model().attributeExists("productCreateBindingModel"));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void testCreateConfirm_whenParametersAreValid() throws Exception {
        ProductCreateBindingModel productCreateBindingModel = new ProductCreateBindingModel();
        productCreateBindingModel.setCode(UUID.randomUUID().toString());
        productCreateBindingModel.setName(UUID.randomUUID().toString());
        productCreateBindingModel.setMade(UUID.randomUUID().toString());
        productCreateBindingModel.setDescription(UUID.randomUUID().toString());
        productCreateBindingModel.setImgUrl("https://www.edenbrothers.com/store/media/Seeds-Vegetables/Tomato-Bradley.jpg");
        productCreateBindingModel.setPrice(BigDecimal.TEN);
        productCreateBindingModel.setType(ProductType.FRUIT);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/products/create")
                        .flashAttr("productCreateBindingModel", productCreateBindingModel))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/products"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void testCreateConfirm_whenParametersAreInvalid() throws Exception {
        ProductCreateBindingModel productCreateBindingModel = new ProductCreateBindingModel();

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/products/create")
                        .flashAttr("productCreateBindingModel", productCreateBindingModel))
                .andExpect(status().isFound())
                .andExpect(flash().attributeExists("productCreateBindingModel", "org.springframework.validation.BindingResult.productCreateBindingModel"))
                .andExpect(view().name("redirect:create"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void testGetProductTable() throws Exception {
        Mockito.when(productService.getProductTable()).thenReturn(new ArrayList<>());

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/products/product-table"))
                .andExpect(status().isOk())
                .andExpect(view().name("product/product-table"))
                .andExpect(model().attributeExists("productTableViewModels"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void testDetailsProduct() throws Exception {
        String productId = UUID.randomUUID().toString();

        ProductDetailsServiceModel productDetailsServiceModel = new ProductDetailsServiceModel();
        productDetailsServiceModel.setId(productId);

        Mockito.when(productService.getProductDetailsModel(productId)).thenReturn(productDetailsServiceModel);
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/products/details/" + productId))
                .andExpect(status().isOk())
                .andExpect(view().name("product/details-product"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void testEditProduct() throws Exception {
        String productId = UUID.randomUUID().toString();

        ProductEditServiceModel productEditServiceModel = new ProductEditServiceModel();
        productEditServiceModel.setId(productId);

        Mockito.when(productService.getProductEditModelById(productId)).thenReturn(productEditServiceModel);
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/products/edit/" + productId))
                .andExpect(status().isOk())
                .andExpect(view().name("product/edit-product"))
                .andExpect(model().attributeExists("productEditModel"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void testEditProductConfirm_whenParametersAreValid() throws Exception {
        String productId = UUID.randomUUID().toString();

        ProductEditModel productEditModel = new ProductEditModel();
        productEditModel.setName(UUID.randomUUID().toString());
        productEditModel.setDescription(UUID.randomUUID().toString());
        productEditModel.setImgUrl("https://www.edenbrothers.com/store/media/Seeds-Vegetables/Tomato-Bradley.jpg");
        productEditModel.setPrice(BigDecimal.TEN);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/products/edit/" + productId)
                        .flashAttr("productEditModel", productEditModel))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/products/details/" + productId));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void testEditProductConfirm_whenParametersAreInvalid() throws Exception {
        String productId = UUID.randomUUID().toString();

        ProductEditModel productEditModel = new ProductEditModel();

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/products/edit/" + productId)
                        .flashAttr("productEditModel", productEditModel))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/products/edit/" + productId))
                .andExpect(flash().attributeExists("productEditModel", "org.springframework.validation.BindingResult.productEditModel"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void testDeleteProduct() throws Exception {
        String productId = UUID.randomUUID().toString();

        ProductDeleteServiceModel product = new ProductDeleteServiceModel();

        Mockito.when(productService.getProductDeleteModelById(productId)).thenReturn(product);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/products/delete/" + productId))
                .andExpect(status().isOk())
                .andExpect(view().name("product/delete-product"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void testDeleteProductConfirm() throws Exception {
        String productId = UUID.randomUUID().toString();

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/products/delete/" + productId))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/products/product-table"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void testGetPromoteForm_whenPromotionalPriceIsNull() throws Exception {
        String productId = UUID.randomUUID().toString();
        ProductDetailsServiceModel productServiceModel = new ProductDetailsServiceModel();

        Mockito.when(productService.getProductDetailsModel(productId)).thenReturn(productServiceModel);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/products/promote/" + productId))
                .andExpect(status().isOk())
                .andExpect(view().name("product/promote-form"))
                .andExpect(model().attributeExists("productDetailsModel", "priceDiscountBindingModel"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void testGetPromoteForm_whenPromotionalPriceIsNotNull() throws Exception {
        String productId = UUID.randomUUID().toString();
        ProductDetailsServiceModel productServiceModel = new ProductDetailsServiceModel();
        productServiceModel.setPromotionalPrice(BigDecimal.TEN);

        Mockito.when(productService.getProductDetailsModel(productId)).thenReturn(productServiceModel);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/products/promote/" + productId))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/products/product-table"));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void testPromoteConfirm_whenPriceDiscountBindingModelIsValid() throws Exception {
        String productId = UUID.randomUUID().toString();
        PriceDiscountBindingModel priceDiscountBindingModel = new PriceDiscountBindingModel();
        priceDiscountBindingModel.setDiscount(10);
        priceDiscountBindingModel.setFromDate(LocalDateTime.now().plusMinutes(5));
        priceDiscountBindingModel.setToDate(LocalDateTime.now().plusDays(5));

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/products/promote/" + productId)
                        .flashAttr("priceDiscountBindingModel", priceDiscountBindingModel))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/products/product-table"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void testPromoteConfirm_whenPriceDiscountBindingModelIsInvalid() throws Exception {
        String productId = UUID.randomUUID().toString();
        PriceDiscountBindingModel priceDiscountBindingModel = new PriceDiscountBindingModel();

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/products/promote/" + productId)
                        .flashAttr("priceDiscountBindingModel", priceDiscountBindingModel))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/products/promote/" + productId))
                .andExpect(flash().attributeExists("priceDiscountBindingModel", "org.springframework.validation.BindingResult.priceDiscountBindingModel"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void testGetPromotionalProductTable() throws Exception {
        Mockito.when(productService.getDiscountedProducts(LocalDateTime.now())).thenReturn(new ArrayList<>());

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/products/promotion-table"))
                .andExpect(status().isOk())
                .andExpect(view().name("product/promotion-table"))
                .andExpect(model().attributeExists("products"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void testRemovePromotion() throws Exception {
        String productId = UUID.randomUUID().toString();

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/products/remove-promotion/" + productId))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/products/promotion-table"));
    }
}