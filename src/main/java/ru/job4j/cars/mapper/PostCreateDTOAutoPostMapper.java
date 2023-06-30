package ru.job4j.cars.mapper;

import org.springframework.stereotype.Component;
import ru.job4j.cars.dto.PostCreateDTO;
import ru.job4j.cars.model.AutoPost;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Mark;
import ru.job4j.cars.model.PriceHistory;

@Component
public class PostCreateDTOAutoPostMapper implements Mapper<PostCreateDTO, AutoPost> {
    @Override
    public AutoPost convert(PostCreateDTO dto) {
        var mark = new Mark();
        mark.setId(dto.getMarkId());
        var post = AutoPost.builder()
                .description(dto.getDescription())
                .car(new Car(dto.getCarName(), dto.getOwners(), dto.getColor(), mark))
                .build();
        post.addPriceHistory(new PriceHistory(dto.getPrice()));
        return post;
    }
}
