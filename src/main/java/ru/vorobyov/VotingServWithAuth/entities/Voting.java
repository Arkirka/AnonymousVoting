package ru.vorobyov.VotingServWithAuth.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class Voting {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "id_Sequence")
    @SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
    private int id;

    private String theme;
    private int yes;
    private int no;
    private int neutral;
    private int broken;
    private int userSize;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "voting")
    private List<Vote> voteList;
    @Transient
    private int notVotedSize;
    @Transient
    private double yesValue;
    @Transient
    private double noValue;
    @Transient
    private double brokenValue;
    @Transient
    private double neutralValue;
    @Transient
    private double notVotedValue;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public int getYes() {
        return yes;
    }

    public void setYes(int yes) {
        this.yes = yes;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getNeutral() {
        return neutral;
    }

    public void setNeutral(int neutral) {
        this.neutral = neutral;
    }

    public int getBroken() {
        return broken;
    }

    public void setBroken(int broken) {
        this.broken = broken;
    }

    public int getUserSize() {
        return userSize;
    }

    public void setUserSize(int count) {
        this.userSize = count;
    }

    public double getYesValue() {
        return yesValue;
    }

    public void setYesValue(double yesValue) {
        this.yesValue = yesValue;
    }

    public double getNoValue() {
        return noValue;
    }

    public void setNoValue(double noValue) {
        this.noValue = noValue;
    }

    public double getBrokenValue() {
        return brokenValue;
    }

    public void setBrokenValue(double brokenValue) {
        this.brokenValue = brokenValue;
    }

    public double getNeutralValue() {
        return neutralValue;
    }

    public void setNeutralValue(double neutralValue) {
        this.neutralValue = neutralValue;
    }

    public double getNotVotedValue() {
        return notVotedValue;
    }

    public void setNotVotedValue(double notVotedValue) {
        this.notVotedValue = notVotedValue;
    }

    public int getNotVotedSize() {
        return notVotedSize;
    }

    public void setNotVotedSize(int notVotedSize) {
        this.notVotedSize = notVotedSize;
    }

    public List<Vote> getVoteList() {
        return voteList;
    }

    public void setVoteList(List<Vote> votes) {
        this.voteList = votes;
    }
}
