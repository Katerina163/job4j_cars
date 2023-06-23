package ru.job4j.cars.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "price_history")
@Data
@EqualsAndHashCode(of = "created")
public class PriceHistory implements Comparable<PriceHistory> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long before;

    private long after;

    private LocalDateTime created;

    @ManyToOne(optional = false)
    @JoinColumn(name = "post_id", nullable = false, insertable = false, updatable = false)
    private AutoPost post;

    @Override
    public int compareTo(PriceHistory priceHistory) {
        return this.created.compareTo(priceHistory.getCreated());
    }
}