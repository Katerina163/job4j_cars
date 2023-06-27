package ru.job4j.cars.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Profile {
    private List<Banner> userPosts;
    private List<Banner> userSubscribe;
}
