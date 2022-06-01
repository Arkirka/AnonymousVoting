package ru.vorobyov.VotingServWithAuth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.vorobyov.VotingServWithAuth.entities.RecoveryLink;
import ru.vorobyov.VotingServWithAuth.entities.User;
import ru.vorobyov.VotingServWithAuth.services.EmailService;
import ru.vorobyov.VotingServWithAuth.services.RecoveryLinkService;
import ru.vorobyov.VotingServWithAuth.services.UserDetailsServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@Controller
public class PasswordRecoveryController {
    @Autowired
    private UserDetailsServiceImpl userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private RecoveryLinkService recoveryLinkService;
    @GetMapping("/login/recovery")
    public String getPage(Model model){
        if (!model.containsAttribute("userForm"))
            model.addAttribute("userForm", new User());
        if (!model.containsAttribute("error"))
            model.addAttribute("error", "");
        return "loginrecovery";
    }

    @PostMapping("/login/recovery/password")
    public String redirectToChangePassword(@ModelAttribute("userForm") @Validated User userForm,
                                           BindingResult bindingResult,
                                           Model model, RedirectAttributes redirectAttributes,
                                           HttpServletRequest request) {
        model.addAttribute("error", "");
        if (userForm.getUserName().equals("")  || userForm.getFullName().equals("") || userForm.getEmail().equals("")){
            model.addAttribute("error", "Одно или несколько полей пусты");
            return "loginrecovery";
        }

        if (!userService.isUserExist(userForm)){
            model.addAttribute("error", "Пользователь не найден");
            return "loginrecovery";
        } else {
            sendRecoveryLinkToEmail(userService.findUserByUsername(userForm.getUserName().trim()), request.getRequestURL().toString());
            return "redirect:/login/recovery?recovered";
        }
    }

    @GetMapping("/login/recovery/password/{uuid}")
    public String redirect(@PathVariable String uuid, Model model) throws Exception {
        Optional<RecoveryLink> recoveryLinkOptional = recoveryLinkService.findRecoveryLinkByLink(uuid);
        if (!recoveryLinkOptional.isPresent()){
            throw new Exception("Not found");
        }

        model.addAttribute("userForm", recoveryLinkOptional.get().getUser());
        return "loginrecoverychange";
    }

    @PostMapping("/login/recovery/password/{uuid}")
    public String changePassword(@PathVariable String uuid, @ModelAttribute("userForm") @Validated User userForm,
                                 HttpServletRequest request,
                                 Model model) throws Exception {
        Optional<RecoveryLink> recoveryLinkOptional = recoveryLinkService.findRecoveryLinkByLink(uuid);
        if (!recoveryLinkOptional.isPresent()){
            throw new Exception("Not found");
        }
        if (userForm.getPassword().equals("") || userForm.getPassword().isEmpty()){
            return "redirect:" + request.getRequestURI() + "?notFilled";
        }
        if (userForm.getPassword().equals(userForm.getPasswordRepeat())){
            if (userService.updateUserPassword(userForm)){
                recoveryLinkService.delete(recoveryLinkOptional.get().getId());
                return "redirect:/login?passwordChanged";
            }
            else
                return "redirect:" + request.getRequestURI() + "?notFound";
        } else {
            return "redirect:" + request.getRequestURI() + "?mismatch";
        }
    }

    private void sendRecoveryLinkToEmail(User user, String url){
        String uuid = getUUID();
        addLinkToDb(user, uuid);
        emailService.sendSimpleEmail(user.getEmail(), "Восстановление пароля учетной записи", "Перейдите по ссылке для смены пароля в своей учетной записи: " + url + "/" + uuid);
    }

    private String getUUID(){
        return  UUID.randomUUID().toString();
    }

    private void addLinkToDb(User user, String uuid) {
        RecoveryLink recoveryLink = new RecoveryLink();
        recoveryLink.setLink(uuid);
        recoveryLink.setUser(user);
        recoveryLinkService.create(recoveryLink);
    }

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
}
