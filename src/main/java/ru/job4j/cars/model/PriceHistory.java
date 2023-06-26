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

    private long price;

    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", updatable = false)
    private AutoPost post;

    public PriceHistory(long price) {
        this.price = price;
        created = LocalDateTime.now();
    }

    @Override
    public int compareTo(PriceHistory priceHistory) {
        return this.created.compareTo(priceHistory.getCreated());
    }
}