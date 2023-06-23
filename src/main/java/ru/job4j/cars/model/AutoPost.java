package ru.job4j.cars.model;

import lombok.*;
import org.hibernate.annotations.SortNatural;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "auto_post")
@Data
@EqualsAndHashCode(exclude = {"files", "history"})
@ToString(exclude = {"files", "history"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutoPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date created;

    private boolean sold;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
    @JoinColumn(name = "car_id", nullable = false, unique = true)
    private Car car;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private User author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @SortNatural
    private List<File> files = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @SortNatural
    private SortedSet<PriceHistory> history = new TreeSet<>();

    public void addFile(File file) {
        files.add(file);
        file.setPost(this);
    }

    public void addPriceHistory(PriceHistory priceHistory) {
        history.add(priceHistory);
        priceHistory.setPost(this);
    }
}
