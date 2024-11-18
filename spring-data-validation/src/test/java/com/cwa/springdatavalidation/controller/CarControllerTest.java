package com.cwa.springdatavalidation.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnCarAdded() throws Exception {
        String json = """
            {
                "vin": "1HGCM82633A123456",
                "brand": "Toyota",
                "model": "Corolla",
                "year": 2023,
                "mileage": 0,
                "price": 25000.0
            }
            """;
        mockMvc.perform(post("/api/cars")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vin").value("1HGCM82633A123456"));
    }

    @Test
    void shouldReturnBadRequest() throws Exception {
        String json = """
            {
                "vin": "FGHJJBGH",
                "brand": "Toyota",
                "model": "Corolla",
                "year": 1999,
                "mileage": -1,
                "price": 25000.0
            }
            """;
        mockMvc.perform(post("/api/cars")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.vin").value("Invalid VIN"))
                .andExpect(jsonPath("$.year").value("Invalid year"))
                .andExpect(jsonPath("$.mileage").value("Mileage can not be negative"));
    }
}