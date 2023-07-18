package ru.job4j.cars.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Profile {
    private List<Banner> userPosts = new ArrayList<>();
    private List<Banner> userSubscribe = new ArrayList<>();
}
