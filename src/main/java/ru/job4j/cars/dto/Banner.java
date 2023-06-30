package ru.job4j.cars.dto;

import lombok.*;

import java.time.LocalDateTime;

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

    @Override
    public int compareTo(Banner ban) {
        return created.compareTo(ban.created);
    }
}