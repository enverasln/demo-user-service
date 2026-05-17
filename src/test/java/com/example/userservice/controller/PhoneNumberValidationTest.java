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
class PhoneNumberValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void acceptsValidE164PhoneNumber() throws Exception {
        String body = """
                { "username": "testuser", "email": "test@example.com", "password": "Password123", "phoneNumber": "+905551234567" }
                """;

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("+905551234567"))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    void rejectsInvalidPhoneNumberFormat() throws Exception {
        String body = """
                { "username": "testuser", "email": "test@example.com", "password": "Password123", "phoneNumber": "123456789" }
                """;

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("phone number must be in E.164 format (e.g., +905551234567)"));
    }

    @Test
    void acceptsNullPhoneNumber() throws Exception {
        String body = """
                { "username": "testuser", "email": "test@example.com", "password": "Password123", "phoneNumber": null }
                """;

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.phoneNumber").isEmpty())
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    void acceptsOmittedPhoneNumber() throws Exception {
        String body = """
                { "username": "testuser", "email": "test@example.com", "password": "Password123" }
                """;

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.phoneNumber").isEmpty())
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    void rejectsPhoneNumberWithoutPlusSign() throws Exception {
        String body = """
                { "username": "testuser", "email": "test@example.com", "password": "Password123", "phoneNumber": "905551234567" }
                """;

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("phone number must be in E.164 format (e.g., +905551234567)"));
    }

    @Test
    void rejectsPhoneNumberStartingWithZero() throws Exception {
        String body = """
                { "username": "testuser", "email": "test@example.com", "password": "Password123", "phoneNumber": "+0551234567" }
                """;

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("phone number must be in E.164 format (e.g., +905551234567)"));
    }
}