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
class ValidationErrorResponseTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnsStructuredErrorResponseForInvalidEmail() throws Exception {
        String body = """
                { "email": "invalid-email", "password": "Password123" }
                """;

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.detail").value("email must be a valid email address"))
                .andExpect(jsonPath("$.path").value("/users/register"));
    }

    @Test
    void returnsStructuredErrorResponseForBlankPassword() throws Exception {
        String body = """
                { "email": "alice@example.com", "password": "" }
                """;

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.detail").value("password is required"))
                .andExpect(jsonPath("$.path").value("/users/register"));
    }

    @Test
    void returnsStructuredErrorResponseForBlankEmail() throws Exception {
        String body = """
                { "email": "", "password": "Password123" }
                """;

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.detail").value("email is required"))
                .andExpect(jsonPath("$.path").value("/users/register"));
    }

    @Test
    void validRegistrationStillWorks() throws Exception {
        String body = """
                { "email": "alice@example.com", "password": "Password123" }
                """;

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    void returnsFirstValidationErrorWhenMultipleErrorsExist() throws Exception {
        String body = """
                { "email": "invalid-email", "password": "123" }
                """;

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.path").value("/users/register"));
    }
}