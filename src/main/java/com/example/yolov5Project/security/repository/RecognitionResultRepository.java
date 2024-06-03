package com.example.yolov5Project.security.repository;

import com.example.yolov5Project.security.entity.RecognitionResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecognitionResultRepository extends JpaRepository<RecognitionResult, Long> {
}
