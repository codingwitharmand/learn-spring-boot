package com.cwa.springdatavalidation.controller;

import com.cwa.springdatavalidation.entity.Car;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final List<Car> cars = new ArrayList<>();

    @PostMapping
    public ResponseEntity<Car> createCar(@Valid @RequestBody Car car) {
        cars.add(car);
        return ResponseEntity.ok(cars.get(cars.size() - 1));
    }
}
