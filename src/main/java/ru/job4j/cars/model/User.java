package ru.job4j.cars.model;

import lombok.*;
import org.hibernate.annotations.SortNatural;

import javax.persistence.*;
import java.util.SortedSet;
import java.util.TreeSet;

@NamedEntityGraph(
        name = "profile",
        attributeNodes = {
                @NamedAttributeNode(value = "participates", subgraph = "priceHistory"),
                @NamedAttributeNode(value = "userPosts", subgraph = "priceHistory")
        },
        subgraphs = {
                @NamedSubgraph(name = "priceHistory", attributeNodes = {
                        @NamedAttributeNode(value = "car", subgraph = "CarWithMark"),
                        @NamedAttributeNode("files"),
                        @NamedAttributeNode("history"),
                        @NamedAttributeNode("author")
                }),
                @NamedSubgraph(name = "CarWithMark",
                        attributeNodes = @NamedAttributeNode("mark"))
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

    @SortNatural
    @ManyToMany
    @JoinTable(
            name = "participates",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private SortedSet<AutoPost> participates = new TreeSet<>();

    @SortNatural
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private SortedSet<AutoPost> userPosts = new TreeSet<>();

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