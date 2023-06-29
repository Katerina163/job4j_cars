package ru.job4j.cars.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "revinfo")
@AllArgsConstructor
@NoArgsConstructor
@Data
@RevisionEntity
public class Revision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    @Column(name = "rev")
    private Long id;

    @Column(name = "revtstmp")
    @RevisionTimestamp
    private Date timestamp;

    @Column(name = "user_id")
    private Long userId;


    /**
     * здесь должно происходить сохранение имени из контекста
     * SecurityContextHolder.getContext().getAuthentication().getPrincipal();
     * но в проекте авторизация реализована по-другому
     */
    @PrePersist
    public void onCreate() {
        setUserId(1L);
    }
}
