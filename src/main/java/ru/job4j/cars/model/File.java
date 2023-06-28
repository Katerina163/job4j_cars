package ru.job4j.cars.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Table(name = "files")
@Getter
@Setter
@EqualsAndHashCode(of = "path", callSuper = true)
@NoArgsConstructor
@Audited
public class File extends BaseId<Long> implements Comparable<File> {

    private String name;

    @Column(unique = true)
    private String path;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false, updatable = false)
    private AutoPost post;

    public File(String name, String path) {
        this.name = name;
        this.path = path;
    }

    @Override
    public int compareTo(File file) {
        return path.compareTo(file.getPath());
    }
}
