package ru.job4j.cars.dto;

import lombok.*;
import ru.job4j.cars.model.AutoPost;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
public class Banner implements Comparable<Banner> {
    private Long postId;
    private String carName;
    private String markName;
    private Long price;
    private LocalDateTime created;
    private long fileId;

    private Banner(AutoPost post) {
        this.setPostId(post.getId());
        this.setCarName(post.getCar().getName());
        this.setMarkName(post.getCar().getMark().getName());
        this.setPrice(post.getHistory().last().getPrice());
        this.setCreated(post.getCreated());
        if (!post.getFiles().isEmpty()) {
            this.setFileId(post.getFiles().last().getId());
        }
    }

    public List<Banner> convert(Collection<AutoPost> posts) {
        return posts.stream().map(Banner::new).toList();
    }

    @Override
    public int compareTo(Banner ban) {
        return created.compareTo(ban.created);
    }
}