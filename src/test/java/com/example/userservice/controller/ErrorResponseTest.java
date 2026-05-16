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
class ErrorResponseTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnsDetailedErrorResponseForValidationFailures() throws Exception {
        String body = """
                { "email": "invalid-email", "password": "123" }
                """;

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details.length()").value(3))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void returnsDetailedErrorResponseForBlankFields() throws Exception {
        String body = """
                { "email": "", "password": "" }
                """;

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details[*]").value(org.hamcrest.Matchers.hasItems(
                        org.hamcrest.Matchers.containsString("email: email is required"),
                        org.hamcrest.Matchers.containsString("password: password is required")
                )))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
