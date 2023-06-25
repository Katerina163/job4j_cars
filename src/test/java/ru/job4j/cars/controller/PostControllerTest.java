package ru.job4j.cars.controller;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.cars.dto.Banner;
import ru.job4j.cars.model.*;
import ru.job4j.cars.service.AutoPostService;
import ru.job4j.cars.service.MarkService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {
    @MockBean
    private AutoPostService service;
    @MockBean
    private MarkService markService;
    @Autowired
    private MockMvc mockMvc;

    @Before
    public void before() {
        when(markService.findAll()).thenReturn(List.of(new Mark("mark")));
    }

    @Test
    public void shouldFindAll() throws Exception {
        when(service.findAll()).thenReturn(List.of(new Banner()));
        this.mockMvc.perform(get("/post/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/post/list"))
                .andExpect(model().attributeExists("posts", "marks", "colors"));
    }

    @Test
    public void shouldFindNew() throws Exception {
        when(service.findAllNew()).thenReturn(List.of(new Banner()));
        this.mockMvc.perform(get("/post/new"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/post/list"))
                .andExpect(model().attributeExists("posts", "marks", "colors"));
    }

    @Test
    public void shouldFindWithFile() throws Exception {
        when(service.findWithFile()).thenReturn(List.of(new Banner()));
        this.mockMvc.perform(get("/post/with-photo"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/post/list"))
                .andExpect(model().attributeExists("posts", "marks", "colors"));
    }

    @Test
    public void shouldFindWithMark() throws Exception {
        when(service.findByCarBrand("Audi")).thenReturn(List.of(new Banner()));
        this.mockMvc.perform(post("/post/brand")
                        .param("brand", "Audi"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/post/list"))
                .andExpect(model().attributeExists("posts", "marks", "colors"));
    }

    @Test
    public void shouldNotFindWithMark() throws Exception {
        when(service.findByCarBrand("hi")).thenReturn(Collections.emptyList());
        this.mockMvc.perform(post("/post/brand")
                        .param("brand", "hi"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/error"))
                .andExpect(model().attributeExists("message", "marks", "colors"));
    }

    @Test
    public void shouldFindByMark() throws Exception {
        when(service.findByMark(1L)).thenReturn(List.of(new Banner()));
        this.mockMvc.perform(get("/post/mark/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/post/list"))
                .andExpect(model().attributeExists("posts", "marks", "colors"));
    }

    @Test
    public void shouldFindByColor() throws Exception {
        when(service.findByColor(Color.RED)).thenReturn(List.of(new Banner()));
        this.mockMvc.perform(get("/post/color/RED"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/post/list"))
                .andExpect(model().attributeExists("posts", "marks", "colors"));
    }

    @Test
    public void shouldNotReturnPostPage() throws Exception {
        when(service.findById(1L)).thenReturn(Optional.empty());
        this.mockMvc.perform(get("/post/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/error"))
                .andExpect(model().attributeExists("message", "marks", "colors"));
    }

    @Test
    public void shouldReturnPostPage() throws Exception {
        var post = AutoPost.builder()
                .description("desc")
                .sold(false)
                .car(new Car("car", "owners", Color.RED, new Mark("mark")))
                .author(new User("login", "user"))
                .files(new TreeSet<>())
                .history(new TreeSet<>())
                .build();
        var price = new PriceHistory(100L);
        post.addPriceHistory(price);
        when(service.findById(1L)).thenReturn(Optional.of(post));
        this.mockMvc.perform(get("/post/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/post/post"))
                .andExpect(model().attributeExists("post", "marks", "colors"));
    }
}