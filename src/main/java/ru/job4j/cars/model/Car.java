package ru.job4j.cars.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NamedEntityGraph(
        name = "CarWithMark",
        attributeNodes = {
                @NamedAttributeNode("mark")
        }
)
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
public class Car extends BaseId<Long> {

    private String name;

    private String owners;

    @Enumerated(EnumType.STRING)
    private Color color;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mark_id", updatable = false)
    private Mark mark;

    public Car(String name, String owners, Color color, Mark mark) {
        this.name = name;
        this.owners = owners;
        this.color = color;
        this.mark = mark;
    }
}