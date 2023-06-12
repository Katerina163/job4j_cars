package ru.job4j.cars.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "price_history")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PriceHistory implements Comparable<PriceHistory> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;

    private long before;

    private long after;

    private Date created;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private AutoPost post;

    @Override
    public int compareTo(PriceHistory priceHistory) {
        return Long.compare(this.id, priceHistory.id);
    }
}