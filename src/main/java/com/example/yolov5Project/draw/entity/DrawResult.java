package com.example.yolov5Project.draw.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Table(name="draw")
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class DrawResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "drawurl")
    private String drawimageUrl;
    @Column(name = "drawobjectname")
    private String drawobjectName;
    @Column(name = "drawconfidence")
    private double drawconfidence;
}
