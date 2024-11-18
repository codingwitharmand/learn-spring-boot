package com.cwa.springdatavalidation.entity;

import com.cwa.springdatavalidation.validator.ValidYear;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class Car {

    @NotNull
    @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "Invalid VIN")
    private String vin;

    @NotBlank
    @Size(min = 2, max = 30, message = "Brand must be between 2 and 30 characters")
    private String brand;

    @NotBlank
    @Size(min = 2, max = 30, message = "Model must be between 2 and 30 characters")
    private String model;

    @ValidYear(message= "Invalid year")
    private int year;

    @PositiveOrZero(message = "Mileage can not be negative")
    private double mileage;

    @NotNull(message = "Price can not be null")
    @Positive(message = "Price must be positive")
    private Double price;

}
