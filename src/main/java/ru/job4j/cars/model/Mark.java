package ru.job4j.cars.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "Marks")
public class Mark extends BaseId<Long> implements Serializable {
    private String name;

    public Mark(String name) {
        this.name = name;
    }
}
