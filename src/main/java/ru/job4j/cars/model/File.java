package ru.job4j.cars.model;

import lombok.*;

import javax.persistence.Entity;

import javax.persistence.*;

@Entity
@Table(name = "files")
@Getter
@Setter
@EqualsAndHashCode(of = "path")
@NoArgsConstructor
public class File implements Comparable<File> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Column(unique = true)
    private String path;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false, updatable = false)
    private AutoPost post;

    public File(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public File(long id) {
        this.id = id;
    }

    @Override
    public int compareTo(File file) {
        return path.compareTo(file.getPath());
    }
}
