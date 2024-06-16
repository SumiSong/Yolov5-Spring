package com.example.yolov5Project.study.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Table(name="webcam")
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RecognitionResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "weburl")
    private String imageUrl;
    @Column(name = "webobjectname")
    private String objectName;
    @Column(name = "webconfidence")
    private double confidence;

}



