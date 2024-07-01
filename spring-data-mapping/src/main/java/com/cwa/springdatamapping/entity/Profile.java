package com.cwa.springdatamapping.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bio;

    @OneToOne(mappedBy = "profile")
    private User user;
}
