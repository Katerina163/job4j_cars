package ru.job4j.cars.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "price_history")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PriceHistory extends BaseId<Long> implements Comparable<PriceHistory> {

    private long before;

    private long after;

    private LocalDateTime created;

    @ManyToOne(optional = false)
    @JoinColumn(name = "post_id", nullable = false, updatable = false)
    private AutoPost post;

    public PriceHistory(long before, long after) {
        this.before = before;
        this.after = after;
        created = LocalDateTime.now();
    }

    @Override
    public int compareTo(PriceHistory priceHistory) {
        return this.created.compareTo(priceHistory.getCreated());
    }
}