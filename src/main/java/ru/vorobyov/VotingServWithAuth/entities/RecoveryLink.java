package ru.vorobyov.VotingServWithAuth.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "RecoveryLink")

public class RecoveryLink {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Sequence")
    private int id;
    @Column(unique = true)
    private String link;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", referencedColumnName = "id", unique = true, nullable = false)
    private User user;

    public RecoveryLink(String link, User user) {
        this.link = link;
        this.user = user;
    }

    public RecoveryLink(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecoveryLink that = (RecoveryLink) o;
        return getId() == that.getId() && getLink().equals(that.getLink());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLink());
    }
}
