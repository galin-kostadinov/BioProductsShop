package org.gkk.bioshopapp.web.filter;

import org.gkk.bioshopapp.service.service.AuthenticatedUserService;
import org.gkk.bioshopapp.web.model.order.OrderProductModel;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@Component
public class UserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final AuthenticatedUserService authenticatedUserService;

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public UserAuthenticationSuccessHandler(AuthenticatedUserService authenticatedUserService) {
        super();
        this.authenticatedUserService = authenticatedUserService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, org.springframework.security.core.Authentication authentication) throws IOException, ServletException {
        String username = authenticatedUserService.getUsername();
        httpServletRequest.getSession().setAttribute("username", username);
        httpServletRequest.getSession().setAttribute("cart", new HashMap<String, OrderProductModel>());

        redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/home");
    }
}