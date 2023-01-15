package ru.vorobyov.VotingServWithAuth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.vorobyov.VotingServWithAuth.entities.RecoveryLink;
import ru.vorobyov.VotingServWithAuth.entities.User;
import ru.vorobyov.VotingServWithAuth.services.implementations.UserDetailsServiceImpl;
import ru.vorobyov.VotingServWithAuth.services.interfaces.EmailService;
import ru.vorobyov.VotingServWithAuth.services.interfaces.RecoveryLinkService;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class PasswordRecoveryController {
    private final UserDetailsServiceImpl userService;
    private final EmailService emailService;
    private final RecoveryLinkService recoveryLinkService;

    public PasswordRecoveryController(UserDetailsServiceImpl userService, EmailService emailService, RecoveryLinkService recoveryLinkService) {
        this.userService = userService;
        this.emailService = emailService;
        this.recoveryLinkService = recoveryLinkService;
    }

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
                                           Model model,
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
        RecoveryLink recoveryLink = recoveryLinkService.findRecoveryLinkByLink(uuid).get();
        if (recoveryLink == null){
            throw new Exception("Not found");
        }
        model.addAttribute("userForm", recoveryLink.getUser());
        return "loginrecoverychange";
    }

    @PostMapping("/login/recovery/password/{uuid}")
    public String changePassword(@PathVariable String uuid, @ModelAttribute("userForm") @Validated User userForm,
                                 HttpServletRequest request) throws Exception {
        RecoveryLink recoveryLink = recoveryLinkService.findRecoveryLinkByLink(uuid).get();
        StringBuilder redirect = new StringBuilder("redirect:");
        if (recoveryLink == null){
            throw new Exception("Not found");
        }
        if (userForm.getPassword().equals("") || userForm.getPassword().isEmpty()){
            return redirect.append(request.getRequestURI()).append("?notFilled").toString();
        }
        if (userForm.getPassword().equals(userForm.getPasswordRepeat())){
            if (userService.updateUserPassword(userForm)){
                recoveryLinkService.delete(recoveryLink.getId());
                return redirect.append("/login?passwordChanged").toString();
            }
            else
                return redirect.append(request.getRequestURI()).append("?notFound").toString();
        } else {
            return redirect.append(request.getRequestURI()).append("?mismatch").toString();
        }
    }

    private void sendRecoveryLinkToEmail(User user, String url){
        String uuid = getUUID();
        addLinkToDb(user, uuid);
        StringBuilder message = new StringBuilder("Перейдите по ссылке для смены пароля в своей учетной записи: ");
        emailService.sendSimpleEmail(
                user.getEmail(),
                "Восстановление пароля учетной записи",
                message.append(url).append("/") .append(uuid).toString()
        );
    }

    private String getUUID(){
        return  UUID.randomUUID().toString();
    }

    private void addLinkToDb(User user, String uuid) {
        if (user == null || uuid == null )
            throw new NullPointerException("User or uuid is empty!");
        if (uuid.isEmpty())
            throw new IllegalArgumentException("Uuid must not be empty!");
        recoveryLinkService.create(new RecoveryLink(uuid, user));
    }

}
