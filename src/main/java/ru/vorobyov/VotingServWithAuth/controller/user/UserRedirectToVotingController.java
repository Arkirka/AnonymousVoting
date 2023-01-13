package ru.vorobyov.VotingServWithAuth.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.vorobyov.VotingServWithAuth.dataToObject.UserVotingProcessDto;
import ru.vorobyov.VotingServWithAuth.entities.User;
import ru.vorobyov.VotingServWithAuth.services.implementations.UserDetailsServiceImpl;
import ru.vorobyov.VotingServWithAuth.services.interfaces.VotingService;

import static ru.vorobyov.VotingServWithAuth.controller.voter.VoterProcessController.getUserVotingProcessDto;

@Controller
public class UserRedirectToVotingController {
    @Autowired
    public VotingService votingService;
    @Autowired
    private UserDetailsServiceImpl userService;

    @GetMapping("/user/redirect")
    public String registration(Model model) {
        changeRole();
        UserVotingProcessDto votingForm = getUserVotingProcessDtoWithThemes();
        if (!model.containsAttribute("votingForm"))
            model.addAttribute("votingForm", votingForm);
        if (!model.containsAttribute("error"))
            model.addAttribute("error", "");
        return "votervotingprocess";
    }

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    private void changeRole(){
        User user = userService.findUserByUsername(getCurrentUsername());
        if (userService.updateUserDefaultToVoter(user))
            throw new UsernameNotFoundException("User not found");
    }

    private UserVotingProcessDto getUserVotingProcessDtoWithThemes(){
        return getUserVotingProcessDto(votingService);
    }
}
