package ru.job4j.cars.model;

import lombok.*;
import org.hibernate.annotations.SortNatural;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.SortedSet;
import java.util.TreeSet;

@NamedEntityGraph(
        name = "All",
        attributeNodes = {
                @NamedAttributeNode(value = "car", subgraph = "car.mark"),
                @NamedAttributeNode("files"),
                @NamedAttributeNode("history"),
                @NamedAttributeNode("author")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "car.mark",
                        attributeNodes = {
                                @NamedAttributeNode("mark")
                        }
                )
        }
)
@Entity
@Table(name = "auto_post")
@Data
@EqualsAndHashCode(exclude = {"files", "history"}, callSuper = true)
@ToString(exclude = {"files", "history"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Audited
public class AutoPost extends BaseId<Long> {
    @Builder.Default
    @Version
    private int version = 1;

    private String description;

    @Builder.Default
    private LocalDateTime created = LocalDateTime.now();

    @Builder.Default
    private boolean sold = false;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, optional = false)
    @JoinColumn(name = "car_id", nullable = false, unique = true)
    private Car car;

    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User author;

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @SortNatural
    private SortedSet<File> files = new TreeSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @SortNatural
    private SortedSet<PriceHistory> history = new TreeSet<>();

    public void addFile(File file) {
        files.add(file);
        file.setPost(this);
    }

    public void addPriceHistory(PriceHistory priceHistory) {
        priceHistory.setPost(this);
        history.add(priceHistory);
    }
}
