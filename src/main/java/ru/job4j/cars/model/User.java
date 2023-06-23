package ru.job4j.cars.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "auto_user")
@Data
@EqualsAndHashCode(of = "login")
@ToString(exclude = {"participates", "userPosts"})
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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
}