package ru.job4j.cars.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.job4j.cars.model.AutoPost;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Banner {
    private long postId;
    private String carName;
    private String markName;
    private long price;
    private LocalDateTime created;
    private long fileId;

    public Banner(AutoPost post) {
        this.setPostId(post.getId());
        this.setCarName(post.getCar().getName());
        this.setMarkName(post.getCar().getMark().getName());
        this.setPrice(post.getHistory().last().getPrice());
        this.setCreated(post.getCreated());
        if (!post.getFiles().isEmpty()) {
            this.setFileId(post.getFiles().last().getId());
        }
    }
}