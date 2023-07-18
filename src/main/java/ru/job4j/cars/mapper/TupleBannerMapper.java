package ru.job4j.cars.mapper;

import com.querydsl.core.Tuple;
import org.springframework.stereotype.Component;
import ru.job4j.cars.dto.Banner;

import java.util.Objects;

import static ru.job4j.cars.model.QAutoPost.autoPost;
import static ru.job4j.cars.model.QCar.car;
import static ru.job4j.cars.model.QFile.file;
import static ru.job4j.cars.model.QPriceHistory.priceHistory;

@Component
public class TupleBannerMapper implements Mapper<Tuple, Banner> {
    @Override
    public Banner convert(Tuple tuple) {
        var banner = Banner.builder()
                .postId(tuple.get(autoPost.id))
                .created(tuple.get(autoPost.created))
                .carName(tuple.get(car.name))
                .markName(tuple.get(car.mark.name))
                .build();
        if (!Objects.isNull(tuple.get(priceHistory.price))) {
            banner.setPrice(tuple.get(priceHistory.price));
        }
        if (!Objects.isNull(tuple.get(file.id))) {
            banner.setFileId(tuple.get(file.id));
        }
        return banner;
    }
}
