package ru.vorobyov.VotingServWithAuth.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.vorobyov.VotingServWithAuth.dataToObject.AdditionVotersToVotingDto;
import ru.vorobyov.VotingServWithAuth.entities.User;
import ru.vorobyov.VotingServWithAuth.services.EmailService;
import ru.vorobyov.VotingServWithAuth.services.UserDetailsServiceImpl;
import ru.vorobyov.VotingServWithAuth.services.UserRoles;
import ru.vorobyov.VotingServWithAuth.services.VotingService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AdminVotingInviteController {
    @Autowired
    private UserDetailsServiceImpl userService;
    @Autowired
    private VotingService votingService;
    @Autowired
    private EmailService emailService;
    @GetMapping("/admin/voting/invite")
    public String getTable(Model model) {
        AdditionVotersToVotingDto userForm = getAdditionVotersToVotingDtoWIthUsersFromDb();
        if (!model.containsAttribute("userForm"))
            model.addAttribute("userForm", userForm);
        if (!model.containsAttribute("error"))
            model.addAttribute("error", "");
        return "addvoterstovotingtable";
    }

    @PostMapping("/admin/voting/invite")
    public String addUser(@ModelAttribute("userForm") @Validated AdditionVotersToVotingDto form) {
        changeCheckedUsersRoleToVoter(form.getUserList());
        updateVotingUserSize(form.getUserList().stream().filter(el -> el.getIsVoter()).collect(Collectors.toList()).size());
        return "redirect:/admin/voting/result";
    }

    private AdditionVotersToVotingDto getAdditionVotersToVotingDtoWIthUsersFromDb(){
        AdditionVotersToVotingDto userForm = new AdditionVotersToVotingDto();
        List<User> allUsers =  userService.allUsers().stream().filter(el -> el.getRoles().equals(UserRoles.ROLE_USER.toString())).collect(Collectors.toList());
        userForm.addAllUsers(allUsers);
        return userForm;
    }

    private void changeCheckedUsersRoleToVoter(List<User> userList){
        for (User user : userList){
            if (user.getIsVoter()){
                changeRole(user.getId());
                sendInviteToEmail(user.getEmail());
            }
        }
    }

    private void changeRole(int id){
        User user = userService.findUserById(id);
        if (!userService.updateUserDefaultToVoter(user))
            throw new UsernameNotFoundException("User not found");
    }

    private void sendInviteToEmail(String email){
        emailService.sendSimpleEmail(email, "Вас пригласили на голосование!", "Перейдите в личный кабинет для присоединения к голосованию: " + " http://localhost:8080/user/profile");
    }

    private void updateVotingUserSize(int userSize){
        votingService.updateVotingSize(userSize);
    }
}
