package ru.job4j.cars.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.cars.utill.HibernateTestUtil;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldFindAll() throws Exception {
        this.mockMvc.perform(get("/post/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/post/list"))
                .andExpect(model().attributeExists("posts", "marks", "colors"));
    }

    @Test
    public void shouldFindNew() throws Exception {
        this.mockMvc.perform(get("/post/new"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/post/list"))
                .andExpect(model().attributeExists("posts", "marks", "colors"));
    }

    @Test
    public void shouldFindWithFile() throws Exception {
        this.mockMvc.perform(get("/post/with-photo"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/post/list"))
                .andExpect(model().attributeExists("posts", "marks", "colors"));
    }

    @Disabled
    @Test
    public void shouldFindByBrand() throws Exception {
        HibernateTestUtil.insertPosts();
        this.mockMvc.perform(get("/post/brand")
                        .param("brand", "500"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/post/list"))
                .andExpect(model().attributeExists("posts", "marks", "colors"));
    }

    @Test
    public void shouldNotFindByBrand() throws Exception {
        this.mockMvc.perform(get("/post/brand")
                        .param("brand", "hi"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/error"))
                .andExpect(model().attributeExists("message", "marks", "colors"));
    }

    @Test
    public void shouldFindByMark() throws Exception {
        this.mockMvc.perform(get("/post/mark/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/post/list"))
                .andExpect(model().attributeExists("posts", "marks", "colors"));
    }

    @Test
    public void shouldFindByColor() throws Exception {
        this.mockMvc.perform(get("/post/color/RED"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/post/list"))
                .andExpect(model().attributeExists("posts", "marks", "colors"));
    }

    @Test
    public void shouldNotReturnPostPage() throws Exception {
        this.mockMvc.perform(get("/post/100"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/error"))
                .andExpect(model().attributeExists("message", "marks", "colors"));
    }

    @Test
    public void shouldReturnPostPage() throws Exception {
        HibernateTestUtil.insertPosts();
        HibernateTestUtil.insertPrice();
        this.mockMvc.perform(get("/post/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/post/post"))
                .andExpect(model().attributeExists("post", "marks", "colors"));
    }
}