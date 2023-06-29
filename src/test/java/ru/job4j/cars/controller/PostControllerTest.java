package ru.job4j.cars.controller;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.cars.dto.Banner;
import ru.job4j.cars.dto.Criterion;
import ru.job4j.cars.dto.QPredicate;
import ru.job4j.cars.model.*;
import ru.job4j.cars.service.AutoPostService;
import ru.job4j.cars.service.MarkService;

import java.time.LocalDateTime;
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

    private Banner banner = Banner.builder()
            .postId(1L)
            .created(LocalDateTime.now())
            .markName("Audi")
            .carName("name")
            .price(10L)
            .build();

    @Before
    public void before() {
        when(markService.findAll()).thenReturn(List.of(new Mark("mark")));
    }

    @Test
    public void shouldFindAll() throws Exception {
        when(service.search(new Criterion().findAll(), QPredicate::and)).thenReturn(List.of(banner));
        this.mockMvc.perform(get("/post/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/post/list"))
                .andExpect(model().attributeExists("posts", "marks", "colors"));
    }

    @Test
    public void shouldFindNew() throws Exception {
        when(service.search(new Criterion().fresh(), QPredicate::and)).thenReturn(List.of(banner));
        this.mockMvc.perform(get("/post/new"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/post/list"))
                .andExpect(model().attributeExists("posts", "marks", "colors"));
    }

    @Test
    public void shouldFindWithFile() throws Exception {
        when(service.search(new Criterion().withFile(), QPredicate::and)).thenReturn(List.of(banner));
        this.mockMvc.perform(get("/post/with-photo"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/post/list"))
                .andExpect(model().attributeExists("posts", "marks", "colors"));
    }

    @Ignore
    @Test
    public void shouldFindByBrand() throws Exception {
        when(service.search(new Criterion().addBrand("Audi"), QPredicate::and)).thenReturn(List.of(banner));
        this.mockMvc.perform(post("/post/brand")
                        .param("brand", "Audi"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/post/list"))
                .andExpect(model().attributeExists("posts", "marks", "colors"));
    }

    @Test
    public void shouldNotFindByBrand() throws Exception {
        when(service.search(new Criterion().addBrand("hi"), QPredicate::and)).thenReturn(Collections.emptyList());
        this.mockMvc.perform(post("/post/brand")
                        .param("brand", "hi"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/error"))
                .andExpect(model().attributeExists("message", "marks", "colors"));
    }

    @Test
    public void shouldFindByMark() throws Exception {
        when(service.search(new Criterion().addMarkIds(1L), QPredicate::and)).thenReturn(List.of(banner));
        this.mockMvc.perform(get("/post/mark/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/post/list"))
                .andExpect(model().attributeExists("posts", "marks", "colors"));
    }

    @Test
    public void shouldFindByColor() throws Exception {
        when(service.search(new Criterion().addColor(Color.RED), QPredicate::and)).thenReturn(List.of(banner));
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