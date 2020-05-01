package org.gkk.bioshopapp.web.controller;

import org.gkk.bioshopapp.service.model.user.UserEditProfileServiceModel;
import org.gkk.bioshopapp.service.model.user.UserProfileServiceModel;
import org.gkk.bioshopapp.service.service.UserService;
import org.gkk.bioshopapp.web.model.user.UserEditProfileModel;
import org.gkk.bioshopapp.web.model.user.UserProfileViewModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView getProfile(HttpSession session, ModelAndView model) throws Exception {
        String username = session.getAttribute("username").toString();
        UserProfileServiceModel serviceModel = this.userService.getUserByUsername(username);
        model.addObject("model", this.modelMapper.map(serviceModel, UserProfileViewModel.class));

        return super.view("user/profile", model);
    }

    @GetMapping("profile/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView getEditProfile(HttpSession session, ModelAndView model) throws Exception {
        String username = session.getAttribute("username").toString();
        UserProfileServiceModel serviceModel = this.userService.getUserByUsername(username);
        model.addObject("model", this.modelMapper.map(serviceModel, UserProfileViewModel.class));

        return super.view("user/edit-profile", model);
    }

    @PostMapping("profile/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editProfileConfirm(@ModelAttribute UserEditProfileModel model) {
        UserEditProfileServiceModel serviceModel = this.modelMapper.map(model, UserEditProfileServiceModel.class);

        try {
            this.userService.editUserProfile(serviceModel);
            return super.redirect("/user/profile");
        } catch (Exception e) {
            return super.redirect("/user/profile/edit");
        }
    }

    @GetMapping("/all-users")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public ModelAndView getAllUsers(ModelAndView model) {
        List<UserProfileViewModel> users = this.userService.getAllUsers().stream()
                .map(userService -> this.modelMapper.map(userService, UserProfileViewModel.class))
                .collect(Collectors.toList());

        model.addObject("users", users);

        return super.view("user/all-users", model);
    }

    @PostMapping("/set-admin/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView setAdminRole(@PathVariable String id) {
        this.userService.makeAdmin(id);

        return super.redirect("/user/all-users");
    }

    @PostMapping("/set-user/{id}")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public ModelAndView setUserRole(@PathVariable String id) {
        this.userService.makeUser(id);

        return super.redirect("/user/all-users");
    }
}
