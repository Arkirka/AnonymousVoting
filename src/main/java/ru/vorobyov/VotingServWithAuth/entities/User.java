package ru.vorobyov.VotingServWithAuth.entities;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String userName;
    private String fullName;
    @Column(unique = true)
    private String email;
    private String password;
    @Transient
    private String passwordRepeat;
    @Transient
    private boolean isVoter;
    private boolean isActive;
    private String roles;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private List<ActivateLink> activateLinkList;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private List<RecoveryLink> recoveryLinkList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
    public void setIsActive(boolean active) {
        isActive = active;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }

    public boolean getIsVoter() {
        return isVoter;
    }

    public void setIsVoter(boolean voter) {
        isVoter = voter;
    }

    public boolean isVoter() {
        return isVoter;
    }

    public void setVoter(boolean voter) {
        isVoter = voter;
    }

    public List<ActivateLink> getActivateLinkList() {
        return activateLinkList;
    }

    public void setActivateLinkList(List<ActivateLink> activateLinkList) {
        this.activateLinkList = activateLinkList;
    }

    public List<RecoveryLink> getRecoveryLinkList() {
        return recoveryLinkList;
    }

    public void setRecoveryLinkList(List<RecoveryLink> recoveryLinkList) {
        this.recoveryLinkList = recoveryLinkList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return isVoter() == user.isVoter() && isActive() == user.isActive() && getId().equals(user.getId()) && getUserName().equals(user.getUserName()) && Objects.equals(getFullName(), user.getFullName()) && getEmail().equals(user.getEmail()) && Objects.equals(getPassword(), user.getPassword()) && Objects.equals(getPasswordRepeat(), user.getPasswordRepeat()) && Objects.equals(getRoles(), user.getRoles()) && Objects.equals(getActivateLinkList(), user.getActivateLinkList()) && Objects.equals(getRecoveryLinkList(), user.getRecoveryLinkList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUserName(), getFullName(), getEmail(), getPassword(), getPasswordRepeat(), isVoter(), isActive(), getRoles(), getActivateLinkList(), getRecoveryLinkList());
    }
}
