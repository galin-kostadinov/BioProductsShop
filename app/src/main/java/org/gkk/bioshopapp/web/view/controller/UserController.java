package org.gkk.bioshopapp.web.view.controller;

import org.gkk.bioshopapp.service.model.user.UserEditProfileServiceModel;
import org.gkk.bioshopapp.service.model.user.UserProfileServiceModel;
import org.gkk.bioshopapp.service.service.UserService;
import org.gkk.bioshopapp.web.annotation.PageTitle;
import org.gkk.bioshopapp.web.view.model.user.UserEditProfileBindingModel;
import org.gkk.bioshopapp.web.view.model.user.UserProfileViewModel;
import org.gkk.bioshopapp.web.view.model.user.UserRegisterBindingModel;
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
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ROOT')")
    @PageTitle("Users")
    public ModelAndView getAllUsers(ModelAndView model) {
        List<UserProfileViewModel> users = this.userService.getAllUsers().stream()
                .map(userService -> this.modelMapper.map(userService, UserProfileViewModel.class))
                .collect(Collectors.toList());

        model.addObject("users", users);

        return super.view("user/all-users", model);
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Profile")
    public ModelAndView getProfile(Principal principal, ModelAndView model) throws Exception {
        UserProfileServiceModel serviceModel = this.userService.getUserByUsername(principal.getName());
        model.addObject("model", this.modelMapper.map(serviceModel, UserProfileViewModel.class));

        return super.view("user/profile", model);
    }

    @GetMapping("/profile/edit")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Edit Profile")
    public String getEditProfile(Principal principal, Model model) throws Exception {
        if (model.getAttribute("userEditProfileBindingModel") == null) {
            UserEditProfileBindingModel userEditProfileBindingModel =
                    this.modelMapper.map(this.userService.getUserByUsername(principal.getName()), UserEditProfileBindingModel.class);
            model.addAttribute("userEditProfileBindingModel", userEditProfileBindingModel);
        }

        return "user/edit-profile";
    }

    @PostMapping("/profile/edit")
    @PreAuthorize("isAuthenticated()")
    public String editProfileConfirm(@Valid @ModelAttribute UserEditProfileBindingModel userEditProfileBindingModel,
                                     BindingResult bindingResult, RedirectAttributes redirectAttributes) throws Exception {

        if (userEditProfileBindingModel == null || bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userEditProfileBindingModel", userEditProfileBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userEditProfileBindingModel", bindingResult);
            return this.redirectStr("/users/profile/edit");
        }

        UserEditProfileServiceModel serviceModel = this.modelMapper.map(userEditProfileBindingModel, UserEditProfileServiceModel.class);

        List<String> violations = this.userService.editUserProfile(serviceModel);

        if (violations != null) {
            redirectAttributes.addFlashAttribute("userEditProfileBindingModel", userEditProfileBindingModel);
            redirectAttributes.addFlashAttribute("editProfileError", violations);
            return super.redirectStr("/users/profile/edit");
        }

        return super.redirectStr("/users/profile");
    }

    @PostMapping("/set-admin/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String setAdminRole(@PathVariable String id) {
        this.userService.makeAdmin(id);
        return super.redirectStr("/users");
    }

    @PostMapping("/set-user/{id}")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public String setUserRole(@PathVariable String id) {
        this.userService.makeUser(id);
        return super.redirectStr("/users");
    }
}
