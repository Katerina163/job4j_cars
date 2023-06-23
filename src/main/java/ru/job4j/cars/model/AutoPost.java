package ru.job4j.cars.model;

import lombok.*;
import org.hibernate.annotations.SortNatural;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "auto_post")
@Data
@EqualsAndHashCode(exclude = {"files", "history"}, callSuper = true)
@ToString(exclude = {"files", "history"})
@AllArgsConstructor
public class AutoPost extends BaseId<Long> {

    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date created;

    private boolean sold;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
    @JoinColumn(name = "car_id", nullable = false, unique = true)
    private Car car;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @SortNatural
    private SortedSet<File> files = new TreeSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @SortNatural
    private SortedSet<PriceHistory> history = new TreeSet<>();

    public AutoPost() {
        created = new Date();
    }

    public void addFile(File file) {
        files.add(file);
        file.setPost(this);
    }

    public void addPriceHistory(PriceHistory priceHistory) {
        priceHistory.setPost(this);
        history.add(priceHistory);
    }
}
