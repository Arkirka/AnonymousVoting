package ru.vorobyov.VotingServWithAuth.dataToObject;

import ru.vorobyov.VotingServWithAuth.entities.Voting;

import java.util.ArrayList;
import java.util.List;

public class VotingDefaultDto {
    private List<Voting> votingList;
    private String userSize;
    public VotingDefaultDto(){
        votingList = new ArrayList<>();
    }

    public void addVotingDefault(Voting voting) {
        this.votingList.add(voting);
    }
    public void addAllVotingDefault(List<Voting>  votingList) {
        this.votingList.addAll(votingList);
    }

    public List<Voting> getVotingList() {
        return votingList;
    }

    public void setVotingList(List<Voting> votingList) {
        this.votingList = votingList;
    }

    public String getUserSize() {
        return userSize;
    }

    public void setUserSize(String userSize) {
        this.userSize = userSize;
    }

}
