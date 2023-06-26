package ru.job4j.cars.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.job4j.cars.model.AutoPost;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Banner {
    private Long postId;
    private String carName;
    private String markName;
    private Long price;
    private LocalDateTime created;
    private Long fileId;

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