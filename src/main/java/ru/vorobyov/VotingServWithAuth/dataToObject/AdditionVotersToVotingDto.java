package ru.vorobyov.VotingServWithAuth.dataToObject;

import ru.vorobyov.VotingServWithAuth.dataToObject.data.VoteThemeResult;
import ru.vorobyov.VotingServWithAuth.entities.User;

import java.util.ArrayList;
import java.util.List;

public class AdditionVotersToVotingDto {
    private List<User> userList;
    public AdditionVotersToVotingDto(){
        userList = new ArrayList<>();
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public void addAllUsers(List<User> allUsers) {
        this.userList.addAll(allUsers);
    }
}
