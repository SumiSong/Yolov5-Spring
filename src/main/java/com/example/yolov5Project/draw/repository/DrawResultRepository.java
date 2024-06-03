package com.example.yolov5Project.draw.repository;

import com.example.yolov5Project.draw.entity.DrawResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrawResultRepository extends JpaRepository<DrawResult, Long> {
}