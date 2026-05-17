package com.example.userservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerPasswordValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void rejectsPasswordWithoutUppercaseLetter() throws Exception {
        String body = """
                { "username": "testuser", "email": "alice@example.com", "password": "password123" }
                """;

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejectsPasswordWithoutDigit() throws Exception {
        String body = """
                { "username": "testuser", "email": "alice@example.com", "password": "Password" }
                """;

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejectsPasswordShorterThanEightCharacters() throws Exception {
        String body = """
                { "username": "testuser", "email": "alice@example.com", "password": "Pass1" }
                """;

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void acceptsValidPassword() throws Exception {
        String body = """
                { "username": "testuser", "email": "alice@example.com", "password": "Password123" }
                """;

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }
}