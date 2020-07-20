package org.gkk.bioshopapp.web.view.controller;

import org.gkk.bioshopapp.web.annotation.PageTitle;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController extends BaseController {

    @GetMapping("/")
    @PageTitle("Index")
    public String index(Principal principal) {
        if (principal != null) {
            return super.redirectStr("/home");
        }

        return "index";
    }

    @GetMapping("/home")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Home")
    public String home() {
        return "home";
    }

    @GetMapping("/contacts")
    @PageTitle("Contacts")
    public String contact() {
        return "contacts";
    }
}
