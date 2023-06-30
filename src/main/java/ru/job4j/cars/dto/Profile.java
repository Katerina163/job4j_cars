package ru.job4j.cars.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Profile {
    private List<Banner> userPosts;
    private List<Banner> userSubscribe;
}
