package ru.job4j.cars.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NamedEntityGraph(
        name = "ForProfile",
        attributeNodes = {
                @NamedAttributeNode("participates"),
                @NamedAttributeNode("userPosts")
        }
)
@Entity
@Table(name = "auto_user")
@Data
@EqualsAndHashCode(of = "login", callSuper = true)
@ToString(exclude = {"participates", "userPosts"})
@NoArgsConstructor
public class User  extends BaseId<Long> {

    @Column(unique = true)
    private String login;

    private String password;

    @ManyToMany
    @JoinTable(
            name = "participates",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private Set<AutoPost> participates = new HashSet<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AutoPost> userPosts = new HashSet<>();

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