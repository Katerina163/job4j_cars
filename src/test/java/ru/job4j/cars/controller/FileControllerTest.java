package ru.job4j.cars.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.cars.dto.FileDTO;
import ru.job4j.cars.service.FileService;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FileControllerTest {
    @MockBean
    private FileService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnNotFound() throws Exception {
        when(service.findById(1L)).thenReturn(Optional.empty());
        this.mockMvc.perform(get("/files/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnArrayByte() throws Exception {
        var array = new byte[]{1};
        when(service.findById(1L)).thenReturn(
                Optional.of(new FileDTO("name", 1L, array)));
        this.mockMvc.perform(get("/files/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().bytes(array));
    }
}