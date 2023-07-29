package ru.job4j.cars.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.UserService;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @MockBean
    private UserService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnLoginPage() throws Exception {
        this.mockMvc.perform(get("/user/login"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/user/login"));
    }

    @Test
    public void whenAuthorizationSuccess() throws Exception {
        var user = new User("login", "password");
        when(service.findByLoginAndPassword(user.getLogin(), user.getPassword()))
                .thenReturn(Optional.of(user));

        this.mockMvc.perform(post("/user/login")
                        .param("login", "login")
                        .param("password", "password"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/"));
    }

    @Test
    public void whenAuthorizationFailure() throws Exception {
        var user = new User("login", "password");
        when(service.findByLoginAndPassword(user.getLogin(), user.getPassword()))
                .thenReturn(Optional.empty());

        this.mockMvc.perform(post("/user/login")
                        .param("login", "login")
                        .param("password", "password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/user/login"))
                .andExpect(model().attributeExists("message"));
    }

    @Test
    public void whenRegistration() throws Exception {
        this.mockMvc.perform(get("/user/register"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/user/register"));
    }
}