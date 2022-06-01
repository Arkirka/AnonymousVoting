package ru.vorobyov.VotingServWithAuth.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.vorobyov.VotingServWithAuth.services.UserDetailsServiceImpl;

@Controller
public class UserProfileController {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @GetMapping("/user/profile")
    public String getPage(Model model){
        model.addAttribute("userRole", getRole());
        return "userprofile";
    }

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public String getRole(){
        return userDetailsService.findUserByUsername(getCurrentUsername()).getRoles();
    }
}
