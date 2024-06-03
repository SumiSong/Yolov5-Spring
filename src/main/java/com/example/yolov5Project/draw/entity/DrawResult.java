package com.example.yolov5Project.draw.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class DrawResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String drawimageUrl;
    private String drawobjectName;
    private double drawconfidence;

    // Getter 및 Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDrawimageUrl() {
        return drawimageUrl;
    }

    public void setDrawimageUrl(String drawimageUrl) {
        this.drawimageUrl = drawimageUrl;
    }

    public String getDrawobjectName() {
        return drawobjectName;
    }

    public void setDrawobjectName(String drawobjectName) {
        this.drawobjectName = drawobjectName;
    }

    public double getDrawconfidence() {
        return drawconfidence;
    }

    public void setDrawconfidence(double drawconfidence) {
        this.drawconfidence = drawconfidence;
    }
}
