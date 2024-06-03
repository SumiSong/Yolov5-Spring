package com.example.yolov5Project.draw.service;

import com.example.yolov5Project.draw.entity.DrawResult;
import com.example.yolov5Project.draw.repository.DrawResultRepository;
import com.example.yolov5Project.study.entity.RecognitionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Service
public class DrawResultService {
    private final DrawResultRepository drawResultRepository;
    private final DrawFileStorageService drawFileStorageService;

    @Autowired

    public DrawResultService(DrawResultRepository drawResultRepository, DrawFileStorageService drawFileStorageService) {
        this.drawResultRepository = drawResultRepository;
        this.drawFileStorageService = drawFileStorageService;
    }

    public DrawResult saveResult(DrawResult drawResult, byte[] imageBytes) {
        // 파일 저장
        String fileName = "image_" + System.currentTimeMillis() + ".jpg";
        String imageUrl = "/uploads/" + fileName;  // 절대 경로로 설정
        String filePath = "src/main/resources/static" + imageUrl;  // 실제 파일 시스템 경로

        try {
            java.nio.file.Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());  // 디렉토리 생성
            Files.write(path, imageBytes, StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            throw new RuntimeException("저장 실패 " + fileName, e);
        }

        // 이미지 URL 설정
        drawResult.setDrawimageUrl(imageUrl);

        return drawResultRepository.save(drawResult);
    }

    //모든 결과 조회
    public List<DrawResult> getAllResults() {
        return drawResultRepository.findAll();
    }

    //세부 항목 조회
    public DrawResult getResultById(Long id) {
        return drawResultRepository.findById(id).orElse(null);
    }

}
