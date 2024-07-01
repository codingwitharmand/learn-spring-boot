package com.cwa.springdatamapping.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
