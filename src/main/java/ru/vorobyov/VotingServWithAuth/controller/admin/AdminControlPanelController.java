package ru.vorobyov.VotingServWithAuth.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.vorobyov.VotingServWithAuth.dataToObject.AdditionVotersToVotingDto;
import ru.vorobyov.VotingServWithAuth.entities.User;
import ru.vorobyov.VotingServWithAuth.services.implementations.UserDetailsServiceImpl;

import java.util.List;
@Controller
public class AdminControlPanelController {
    private final UserDetailsServiceImpl userService;

    public AdminControlPanelController(UserDetailsServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/panel")
    public String getPage(Model model){
        AdditionVotersToVotingDto userForm = getAllUsersFromDb();
        if (!model.containsAttribute("userForm"))
            model.addAttribute("userForm", userForm);
        return "admincontrolpanel";
    }

    @GetMapping("/admin/panel/edit/{id}")
    public String getPageUpdateUser(@PathVariable("id") int id, Model model) {
        if (!userService.isUserExist(id))
            return "redirect:/admin/panel?notFound";
        model.addAttribute("userForm", userService.findUserById(id));
        return "adminpaneledituser";
    }

    @PostMapping("/admin/panel/edit/{id}")
    public String updateUser(@PathVariable("id") int id, @ModelAttribute("userForm") @Validated User user) {
        if (userService.updateUser(user)){
            return "redirect:/admin/panel/edit/" + id + "?success";
        }
        return "redirect:/admin/panel?notFound";
    }

    private AdditionVotersToVotingDto getAllUsersFromDb(){
        AdditionVotersToVotingDto userForm = new AdditionVotersToVotingDto();
        List<User> allUsers =  userService.allUsers();
        userForm.addAllUsers(allUsers);
        return userForm;
    }
}
