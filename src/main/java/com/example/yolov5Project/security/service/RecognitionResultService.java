package com.example.yolov5Project.security.service;

import com.example.yolov5Project.security.entity.RecognitionResult;
import com.example.yolov5Project.security.repository.RecognitionResultRepository;
import jakarta.persistence.criteria.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Service
public class RecognitionResultService {

    private final RecognitionResultRepository recognitionResultRepository;
    private final FileStorageService fileStorageService;

    @Autowired
    public RecognitionResultService(RecognitionResultRepository recognitionResultRepository, FileStorageService fileStorageService) {
        this.recognitionResultRepository = recognitionResultRepository;
        this.fileStorageService = fileStorageService;
    }

    public RecognitionResult saveResult(RecognitionResult recognitionResult, byte[] imageBytes) {
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
        recognitionResult.setImageUrl(imageUrl);

        return recognitionResultRepository.save(recognitionResult);
    }

    //모든 결과 조회
    public List<RecognitionResult> getAllResults() {
        return recognitionResultRepository.findAll();
    }

    //세부 항목 조회
    public RecognitionResult getResultById(Long id) {
        return recognitionResultRepository.findById(id).orElse(null);
    }
}
