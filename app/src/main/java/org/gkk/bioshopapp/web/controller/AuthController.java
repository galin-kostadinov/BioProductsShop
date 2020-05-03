package org.gkk.bioshopapp.web.controller;

import org.gkk.bioshopapp.error.PriceHishoryNotFoundException;
import org.gkk.bioshopapp.error.RoleNotFoundException;
import org.gkk.bioshopapp.service.model.user.UserRegisterServiceModel;
import org.gkk.bioshopapp.service.service.AuthService;
import org.gkk.bioshopapp.web.annotation.PageTitle;
import org.gkk.bioshopapp.web.model.user.UserRegisterModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController extends BaseController {
    private final AuthService authService;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthController(
            AuthService authService, ModelMapper modelMapper) {
        this.authService = authService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Register")
    public ModelAndView getRegisterForm() {
        return super.view("user/register");
    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView register(@ModelAttribute UserRegisterModel model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return super.view("user/register");
        }

        UserRegisterServiceModel serviceModel = this.modelMapper.map(model, UserRegisterServiceModel.class);
        authService.register(serviceModel);
        return super.redirect("/login");
    }

    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Login")
    public ModelAndView getLoginForm(@RequestParam(required = false) String error) {
        ModelAndView model = new ModelAndView("user/login");

        if (error != null) {
            model.addObject("error", error);
        }

        return model;
    }
}
