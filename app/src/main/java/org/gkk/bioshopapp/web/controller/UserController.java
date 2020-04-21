package org.gkk.bioshopapp.web.controller;

import org.gkk.bioshopapp.service.model.LoginUserServiceModel;
import org.gkk.bioshopapp.service.model.ProductServiceModel;
import org.gkk.bioshopapp.service.model.UserEditProfileServiceModel;
import org.gkk.bioshopapp.service.model.UserProfileServiceModel;
import org.gkk.bioshopapp.service.service.UserService;
import org.gkk.bioshopapp.web.model.UserEditProfileModel;
import org.gkk.bioshopapp.web.model.UserProfileViewModel;
import org.modelmapper.ModelMapper;
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

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/profile")
    public ModelAndView getProfile(HttpSession session, ModelAndView model) throws Exception {
        String username = session.getAttribute("username").toString();
        UserProfileServiceModel serviceModel = this.userService.getUserByUsername(username);
        model.addObject("model", this.modelMapper.map(serviceModel, UserProfileViewModel.class));

        return super.view("user/profile", model);
    }

    @GetMapping("profile/edit")
    public ModelAndView getEditProfile(HttpSession session, ModelAndView model) throws Exception {
        String username = session.getAttribute("username").toString();
        UserProfileServiceModel serviceModel = this.userService.getUserByUsername(username);
        model.addObject("model", this.modelMapper.map(serviceModel, UserProfileViewModel.class));

        return super.view("user/edit-profile", model);
    }

    @PostMapping("profile/edit")
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
    public ModelAndView getAllProducts(ModelAndView model) {
        //admin todo

        List<UserProfileViewModel> users = this.userService.getAllUsers().stream()
                .map(userService -> this.modelMapper.map(userService, UserProfileViewModel.class))
                .collect(Collectors.toList());

        model.addObject("users", users);

        return super.view("user/all-users", model);
    }
}
