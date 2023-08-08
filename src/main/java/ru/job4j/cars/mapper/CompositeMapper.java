package ru.job4j.cars.mapper;

import org.springframework.stereotype.Component;
import ru.job4j.cars.validation.GroupValidation;

import java.util.HashMap;
import java.util.Map;

@Component
public class CompositeMapper {
    private final Map<String, Mapper> map;

    public CompositeMapper() {
        map = new HashMap<>();
        map.put("autoPostBannerMapper", new AutoPostBannerMapper());
        map.put("postCreateDTOAutoPostMapper", new PostCreateDTOAutoPostMapper());
        map.put("postModifyDTOAutoPostMapper", new PostModifyDTOAutoPostMapper());
        map.put("tupleBannerMapper", new TupleBannerMapper());
        map.put("empty", new EmptyMapper());
    }

    public Mapper mapper(Class from, Class to) {
        if (from.getName().contains("AutoPost")
                && to.getName().contains("Banner")) {
            return map.get("autoPostBannerMapper");
        } else if (from.getName().contains("Tuple")
                && to.getName().contains("Banner")) {
            return map.get("tupleBannerMapper");
        }
        return map.get("empty");
    }

    /*
    PostDTO использует валидацию по группам, поэтому дополнительно передается operation
     */
    public Mapper mapper(Class from, Class to, Class<? extends GroupValidation> operation) {
        if (from.getName().contains("PostDTO")
                && to.getName().contains("AutoPost")) {
            if (operation.getName().contains("CreateAction")) {
                return map.get("postCreateDTOAutoPostMapper");
            } else if (operation.getName().contains("ModifyAction")) {
                return map.get("postModifyDTOAutoPostMapper");
            }
        }
        return map.get("empty");
    }
}
