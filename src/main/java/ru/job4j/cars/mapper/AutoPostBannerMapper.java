package ru.job4j.cars.mapper;

import org.springframework.stereotype.Component;
import ru.job4j.cars.dto.Banner;
import ru.job4j.cars.model.AutoPost;

@Component
public class AutoPostBannerMapper implements Mapper<AutoPost, Banner> {
    @Override
    public Banner convert(AutoPost post) {
        var banner = Banner.builder()
                .postId(post.getId())
                .carName(post.getCar().getName())
                .markName(post.getCar().getMark().getName())
                .price(post.getHistory().last().getPrice())
                .created(post.getCreated())
                .build();
        if (!post.getFiles().isEmpty()) {
            banner.setFileId(post.getFiles().last().getId());
        }
        return banner;
    }
}
