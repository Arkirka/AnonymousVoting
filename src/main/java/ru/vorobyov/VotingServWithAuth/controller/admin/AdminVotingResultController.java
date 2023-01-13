package ru.vorobyov.VotingServWithAuth.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.vorobyov.VotingServWithAuth.dataToObject.VotingDefaultDto;
import ru.vorobyov.VotingServWithAuth.entities.Voting;
import ru.vorobyov.VotingServWithAuth.services.implementations.UserDetailsServiceImpl;
import ru.vorobyov.VotingServWithAuth.services.interfaces.VoteService;
import ru.vorobyov.VotingServWithAuth.services.interfaces.VotingService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AdminVotingResultController {
    @Autowired
    public VotingService votingService;
    @Autowired
    public VoteService voteService;
    @Autowired
    private UserDetailsServiceImpl userService;
    @GetMapping("admin/voting/result")
    public String getPage(Model model){
        VotingDefaultDto votingForm = getVotingDefaultDtoWithListFromDb();
        model.addAttribute("votingForm", votingForm);
        model.addAttribute("error", "");
        return "adminvotingresult";
    }

    @PostMapping("admin/voting/result")
    public String update(Model model){
        VotingDefaultDto votingForm = getVotingDefaultDtoWithListFromDb();
        model.addAttribute("votingForm", votingForm);
        model.addAttribute("error", "");
        return "adminvotingresult";
    }

    @PostMapping("admin/voting/result/close")
    public String close(){
        closeVoting();
        return "redirect:/admin/cabinet/";
    }

    public void closeVoting(){
        voteService.deleteAll();
        votingService.deleteAll();
        userService.updateAllVotersToUsers();
    }

    private VotingDefaultDto getVotingDefaultDtoWithListFromDb(){
        VotingDefaultDto votingForm = new VotingDefaultDto();
        List<Voting> votingList = votingService.findAll();
        votingList = votingList.stream().peek(el -> {
            el.setYesValue( (el.getYes() == 0) ? 0 : (el.getYes() / (double) el.getUserSize() * 100) );
            el.setNoValue( (el.getNo() == 0) ? 0 : (el.getNo() / (double) el.getUserSize() * 100));
            el.setNeutralValue((el.getNeutral() == 0) ? 0 : (el.getNeutral() / (double) el.getUserSize() * 100));
            el.setBrokenValue((el.getBroken() == 0) ? 0 : (el.getBroken() / (double) el.getUserSize() * 100));
            el.setNotVotedValue(100 - el.getYesValue() - el.getNoValue() - el.getNeutralValue() - el.getBrokenValue());
            el.setNotVotedSize(el.getUserSize() - el.getYes() - el.getNo() - el.getNeutral() - el.getBroken());
            votingForm.setUserSize(String.valueOf(el.getUserSize()));
        }).collect(Collectors.toList());
        votingForm.addAllVotingDefault(votingList);
        return votingForm;
    }
}
