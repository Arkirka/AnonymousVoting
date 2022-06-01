package ru.vorobyov.VotingServWithAuth.dataToObject;

import ru.vorobyov.VotingServWithAuth.dataToObject.data.VoteThemeResult;

import java.util.ArrayList;
import java.util.List;

public class UserVotingProcessDto {
    private List<VoteThemeResult> voteThemeResultList;
    public UserVotingProcessDto(){
        voteThemeResultList = new ArrayList<>();
    }

    public void addVoteThemeResult(VoteThemeResult voteThemeResult){
        this.voteThemeResultList.add(voteThemeResult);
    }

    public List<VoteThemeResult> getVoteThemeResultList() {
        return voteThemeResultList;
    }

    public void setVoteThemeResultList(List<VoteThemeResult> voteThemeResultList) {
        this.voteThemeResultList = voteThemeResultList;
    }
}
