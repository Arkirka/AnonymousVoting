package ru.vorobyov.VotingServWithAuth.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.vorobyov.VotingServWithAuth.services.VotingService;

@Controller
public class AdminCabinetController {
    @Autowired
    private VotingService votingService;
    @GetMapping("/admin/cabinet")
    public String getPage(Model model) {
        model.addAttribute("votingStatus", getVotingStatus());
        return "admincabinet";
    }

    private String getVotingStatus(){
        int size = votingService.findAll().size();
        if (size == 0){
            return "-1";
        } else{
            return "1";
        }
    }




}
