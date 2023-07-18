package ru.job4j.cars.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NamedEntityGraph(
        name = "withUserPosts",
        attributeNodes = {
                @NamedAttributeNode("userPosts")
        }
)
@Entity
@Table(name = "auto_user")
@Data
@EqualsAndHashCode(of = "login", callSuper = true)
@ToString(exclude = {"participates", "userPosts"})
@NoArgsConstructor
public class User extends BaseId<Long> {

    @Column(unique = true)
    private String login;

    private String password;

    @ManyToMany
    @JoinTable(
            name = "participates",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private List<AutoPost> participates = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AutoPost> userPosts = new ArrayList<>();

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public void addUserPost(AutoPost post) {
        userPosts.add(post);
        post.setAuthor(this);
    }

    public void addParticipates(AutoPost post) {
        participates.add(post);
    }
}