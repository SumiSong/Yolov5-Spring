package com.example.yolov5Project.study.repository;

import com.example.yolov5Project.study.entity.RecognitionResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecognitionResultRepository extends JpaRepository<RecognitionResult, Long> {
}
