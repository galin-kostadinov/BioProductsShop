package org.gkk.bioshopapp.web.controller;

import org.gkk.bioshopapp.service.model.LoginUserServiceModel;
import org.gkk.bioshopapp.service.model.RegisterUserServiceModel;
import org.gkk.bioshopapp.service.service.AuthService;
import org.gkk.bioshopapp.web.model.UserLoginModel;
import org.gkk.bioshopapp.web.model.UserRegisterModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class AuthController extends BaseController {
    private final AuthService authService;
    private final ModelMapper modelMapper;

    public AuthController(
            AuthService authService, ModelMapper modelMapper) {
        this.authService = authService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/register")
    public ModelAndView getRegisterForm() {
        return super.view("user/register");
    }

    @PostMapping("/register")
    public ModelAndView register(@ModelAttribute UserRegisterModel model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return super.view("user/register");
        }

        RegisterUserServiceModel serviceModel = this.modelMapper.map(model, RegisterUserServiceModel.class);
        authService.register(serviceModel);
        return super.redirect("/login");
    }

    @GetMapping("/login")
    public ModelAndView getLoginForm(@RequestParam(required = false) String error) {
        ModelAndView model = new ModelAndView("user/login");

        if (error != null) {
            model.addObject("error", error);
        }

        return model;
    }

    @PostMapping("/login")
    public ModelAndView login(@ModelAttribute UserLoginModel model, HttpSession session) {
        LoginUserServiceModel serviceModel = this.modelMapper.map(model, LoginUserServiceModel.class);

        try {
            LoginUserServiceModel loggedUser = this.authService.login(serviceModel);
            session.setAttribute("username", loggedUser.getUsername());
            return super.redirect("/home");
        } catch (Exception e) {
            return super.redirect("/login");
        }
    }
}
