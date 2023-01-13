package ru.vorobyov.VotingServWithAuth.controller.voter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.vorobyov.VotingServWithAuth.dataToObject.UserVotingProcessDto;
import ru.vorobyov.VotingServWithAuth.dataToObject.data.VoteThemeResult;
import ru.vorobyov.VotingServWithAuth.entities.Vote;
import ru.vorobyov.VotingServWithAuth.entities.Voting;
import ru.vorobyov.VotingServWithAuth.services.implementations.UserDetailsServiceImpl;
import ru.vorobyov.VotingServWithAuth.services.interfaces.VoteService;
import ru.vorobyov.VotingServWithAuth.services.interfaces.VotingService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class VoterProcessController {
    public final VotingService votingService;
    public final VoteService voteService;
    private final UserDetailsServiceImpl userService;

    public VoterProcessController(VotingService votingService, VoteService voteService, UserDetailsServiceImpl userService) {
        this.votingService = votingService;
        this.voteService = voteService;
        this.userService = userService;
    }

    @GetMapping("/voter/voting")
    public String createVoting(Model model) {
        UserVotingProcessDto votingForm = getUserVotingProcessDtoWithThemes();
        if (!model.containsAttribute("votingForm"))
            model.addAttribute("votingForm", votingForm);
        if (!model.containsAttribute("error"))
            model.addAttribute("error", "");
        return "votervotingprocess";
    }

    @PostMapping("/voter/voting")
    public String submit(@ModelAttribute("userForm") @Validated UserVotingProcessDto form) throws Exception {
        List<VoteThemeResult> voteThemeResultList = form.getVoteThemeResultList();
        addVotesToDb(voteThemeResultList);
        addVotingToDb(voteThemeResultList);
        changeUserRoleToDefault();
        return "redirect:/";
    }

    private UserVotingProcessDto getUserVotingProcessDtoWithThemes(){
        return getUserVotingProcessDto(votingService);
    }

    public static UserVotingProcessDto getUserVotingProcessDto(VotingService votingService) {
        UserVotingProcessDto votingForm = new UserVotingProcessDto();
        List<Voting> votingList =  votingService.findAll();
        for (Voting voting : votingList){
            VoteThemeResult tempVoteThemeResult = new VoteThemeResult();
            tempVoteThemeResult.setTheme(voting.getTheme());
            votingForm.addVoteThemeResult(tempVoteThemeResult);
        }
        return votingForm;
    }

    private void addVotesToDb(List<VoteThemeResult> voteThemeResultList){
        List<Vote> voteList = new ArrayList<>();
        List<Voting> votingList = votingService.findAll();
        int iterator = 0;
        for (VoteThemeResult voteThemeResult : voteThemeResultList){
            Vote vote = new Vote();
            if (voteThemeResult.getYes())
                vote.setYes(1);
            if (voteThemeResult.getNo())
                vote.setNo(1);
            if (voteThemeResult.getNeutral())
                vote.setNeutral(1);
            vote.setVoting(votingList.get(iterator));
            iterator++;
            voteList.add(vote);
        }
        voteService.saveAll(voteList);
    }

    private void addVotingToDb(List<VoteThemeResult> voteThemeResultList) throws Exception {
        List<Integer> ids = new ArrayList<>();
        for(Voting voting : votingService.findAll()){
            ids.add(voting.getId());
        }
        for (int i = 0; i < ids.size(); i++){
            if ((voteThemeResultList.get(i).getYes() && voteThemeResultList.get(i).getNo())
                || (voteThemeResultList.get(i).getYes() && voteThemeResultList.get(i).getNeutral())
                || (voteThemeResultList.get(i).getNeutral() && voteThemeResultList.get(i).getNo())){
                votingService.updateUser(ids.get(i), 0, 0, 0, 1);
            }else if (voteThemeResultList.get(i).getYes()){
                votingService.updateUser(ids.get(i), 1, 0, 0, 0);
            } else if (voteThemeResultList.get(i).getNo()){
                votingService.updateUser(ids.get(i), 0, 1, 0, 0);
            } else if (voteThemeResultList.get(i).getNeutral()){
                votingService.updateUser(ids.get(i), 0, 0, 1, 0);
            } else {
                votingService.updateUser(ids.get(i), 0, 0, 0, 1);
            }
        }
    }

    private void changeUserRoleToDefault(){
        if (!userService.updateUserVoterToDefault(getCurrentUsername()))
            throw new UsernameNotFoundException("User not found");
    }

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
}
