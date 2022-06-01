package ru.vorobyov.VotingServWithAuth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import ru.vorobyov.VotingServWithAuth.services.UserDetailsServiceImpl;
import ru.vorobyov.VotingServWithAuth.services.UserRoles;

@Controller
public class MainController {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @GetMapping("/")
    public String redirect(){
        String role = getRole();

        if (role.equals(UserRoles.ROLE_ADMIN.toString()))
            return "redirect:/admin/cabinet/";
        else
            return "redirect:/user/profile/";
    }

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public String getRole(){
        return userDetailsService.findUserByUsername(getCurrentUsername()).getRoles();
    }
}
