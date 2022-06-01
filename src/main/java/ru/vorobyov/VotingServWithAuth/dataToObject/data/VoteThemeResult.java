package ru.vorobyov.VotingServWithAuth.dataToObject.data;

public class VoteThemeResult {
    private String theme;
    private boolean yes;
    private boolean no;
    private boolean neutral;

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public boolean getYes() {
        return yes;
    }

    public void setYes(boolean yes) {
        this.yes = yes;
    }

    public boolean getNo() {
        return no;
    }

    public void setNo(boolean no) {
        this.no = no;
    }

    public boolean getNeutral() {
        return neutral;
    }

    public void setNeutral(boolean neutral) {
        this.neutral = neutral;
    }
}
