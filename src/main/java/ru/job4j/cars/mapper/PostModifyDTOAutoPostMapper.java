package ru.job4j.cars.mapper;

import org.springframework.stereotype.Component;
import ru.job4j.cars.dto.PostDTO;
import ru.job4j.cars.model.AutoPost;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Mark;

@Component
public class PostModifyDTOAutoPostMapper implements Mapper<PostDTO, AutoPost> {
    @Override
    public AutoPost convert(PostDTO dto) {
        var mark = new Mark();
        mark.setId(dto.getMarkId());
        var car = new Car(dto.getCarName(), dto.getOwners(), dto.getColor(), mark);
        car.setId(dto.getCarId());
        var post = AutoPost.builder()
                .description(dto.getDescription())
                .car(car)
                .version(dto.getVersion())
                .build();
        post.setId(dto.getPostId());
        return post;
    }
}
