package com.example.userservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UsernameValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void rejectsUsernameShorterThanThreeCharacters() throws Exception {
        String body = """
                { "username": "ab", "email": "alice@example.com", "password": "Password123" }
                """;

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("username must be between 3 and 30 characters"));
    }

    @Test
    void rejectsUsernameLongerThanThirtyCharacters() throws Exception {
        String body = """
                { "username": "thisusernameiswaytoolongandexceedsthirtychars", "email": "alice@example.com", "password": "Password123" }
                """;

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("username must be between 3 and 30 characters"));
    }

    @Test
    void acceptsValidUsernameThreeCharacters() throws Exception {
        String body = """
                { "username": "abc", "email": "alice@example.com", "password": "Password123" }
                """;

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    void acceptsValidUsernameThirtyCharacters() throws Exception {
        String body = """
                { "username": "abcdefghijklmnopqrstuvwxyz1234", "email": "alice@example.com", "password": "Password123" }
                """;

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    void acceptsValidUsernameMidRange() throws Exception {
        String body = """
                { "username": "testuser", "email": "alice@example.com", "password": "Password123" }
                """;

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    void rejectsBlankUsername() throws Exception {
        String body = """
                { "username": "", "email": "alice@example.com", "password": "Password123" }
                """;

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("username is required"));
    }

    @Test
    void rejectsMissingUsername() throws Exception {
        String body = """
                { "email": "alice@example.com", "password": "Password123" }
                """;

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("username is required"));
    }
}