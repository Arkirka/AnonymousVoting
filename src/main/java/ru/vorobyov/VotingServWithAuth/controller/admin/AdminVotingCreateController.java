package ru.vorobyov.VotingServWithAuth.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.vorobyov.VotingServWithAuth.dataToObject.VotingDefaultDto;
import ru.vorobyov.VotingServWithAuth.entities.Voting;
import ru.vorobyov.VotingServWithAuth.services.VotingService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminVotingCreateController {
    @Autowired
    private VotingService votingService;

    @GetMapping("/admin/voting/create")
    public String createVoting(Model model) {
        VotingDefaultDto votingForm = new VotingDefaultDto();
        for (int i = 0; i < 5; i++)
            votingForm.addVotingDefault(new Voting());
        if (!model.containsAttribute("votingForm"))
            model.addAttribute("votingForm", votingForm);
        if (!model.containsAttribute("error"))
            model.addAttribute("error", "");
        return "admincreatevoting";
    }

    @PostMapping("/admin/voting/create")
    public String addUser(@ModelAttribute("userForm") @Validated VotingDefaultDto form) {
        List<Voting> onlyNotEmptyVotingList = getOnlyNotEmptyVotingDefaultList(form.getVotingList());
        if (onlyNotEmptyVotingList.isEmpty()){
            return "redirect:/admin/voting/create?notFilled";
        }
        votingService.saveAll(onlyNotEmptyVotingList);
        return "redirect:/admin/voting/invite";
    }

    private List<Voting> getOnlyNotEmptyVotingDefaultList(Iterable<Voting> votingList){
        List<Voting> list = new ArrayList();
        for (Voting voting : votingList){
            if (voting.getTheme().equals(""))
                continue;
            else
                list.add(voting);
        }
        return list;
    }
}
