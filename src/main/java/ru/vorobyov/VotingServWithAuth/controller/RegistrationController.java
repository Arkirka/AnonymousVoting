package ru.vorobyov.VotingServWithAuth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.vorobyov.VotingServWithAuth.entities.ActivateLink;
import ru.vorobyov.VotingServWithAuth.entities.User;
import ru.vorobyov.VotingServWithAuth.services.implementations.UserDetailsServiceImpl;
import ru.vorobyov.VotingServWithAuth.services.interfaces.ActivateLinkService;
import ru.vorobyov.VotingServWithAuth.services.interfaces.EmailService;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class RegistrationController {

    private final UserDetailsServiceImpl userService;

    private final EmailService emailService;

    private final ActivateLinkService activateLinkService;

    public RegistrationController(UserDetailsServiceImpl userService, EmailService emailService, ActivateLinkService activateLinkService) {
        this.userService = userService;
        this.emailService = emailService;
        this.activateLinkService = activateLinkService;
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        if (!model.containsAttribute("userForm"))
            model.addAttribute("userForm", new User());
        if (!model.containsAttribute("error"))
            model.addAttribute("error", "");

        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("userForm") @Validated User userForm, BindingResult bindingResult, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        model.addAttribute("error", "");

        if (userForm.getUserName().equals("")  || userForm.getFullName().equals("") || userForm.getEmail().equals("") || userForm.getPassword().equals("") || userForm.getPasswordRepeat().equals("")){
            return "redirect:" + request.getRequestURI() + "?notFilled";
        }

        if (!userForm.getPassword().equals(userForm.getPasswordRepeat())){
            return "redirect:" + request.getRequestURI() + "?mismatch";
        }
        if (!userService.saveUserDefaultUser(userForm)){
            model.addAttribute("error", "Пользователь с таким именем уже существует");
            return "redirect:" + request.getRequestURI() + "?userExist";
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.getFlashAttributes().clear();
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult", bindingResult);
            redirectAttributes.addFlashAttribute("userForm", userForm);
            return "registration";
        }
        sendActivateLinkToEmail(userForm, request.getRequestURL().toString());
        return "redirect:/login?registred";
    }

    @GetMapping("/registration/activate/{uuid}")
    public String activateUser(@PathVariable String uuid) {
        activateLinkService.findActivateLinkByLink(uuid).thenAcceptAsync(result -> {
            userService.changeActiveById(result.getUser().getId(), true);
            activateLinkService.delete(result.getId());
        });
        return "redirect:/login?activated";
    }


    private void sendActivateLinkToEmail(User user, String url){
        String uuid = getUUID();
        addLinkToDb(user, uuid);
        emailService.sendSimpleEmail(user.getEmail(), "Активация учетной записи", "Перейдите по ссылке для активации учетной записи: " + url + "/activate/" + uuid);
    }

    private String getUUID(){
        return  UUID.randomUUID().toString();
    }

    private void addLinkToDb(User user, String uuid){
        ActivateLink activateLink = new ActivateLink();
        activateLink.setLink(uuid);
        activateLink.setUser(user);
        activateLinkService.create(activateLink);
    }
}
