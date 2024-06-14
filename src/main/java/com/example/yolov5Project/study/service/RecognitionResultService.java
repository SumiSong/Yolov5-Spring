package com.example.yolov5Project.study.service;

import com.example.yolov5Project.study.entity.RecognitionResult;
import com.example.yolov5Project.study.repository.RecognitionResultRepository;
import com.example.yolov5Project.websocket.WebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Service
public class RecognitionResultService {

    private final RecognitionResultRepository recognitionResultRepository;
    private final FileStorageService fileStorageService;
    private final WebSocketHandler webSocketHandler;

    @Autowired
    public RecognitionResultService(RecognitionResultRepository recognitionResultRepository, FileStorageService fileStorageService, WebSocketHandler webSocketHandler) {
        this.recognitionResultRepository = recognitionResultRepository;
        this.fileStorageService = fileStorageService;
        this.webSocketHandler = webSocketHandler;
    }

    public RecognitionResult saveResult(RecognitionResult recognitionResult, byte[] imageBytes) {
        String fileName = "image_" + System.currentTimeMillis() + ".jpg";
        String imageUrl = fileStorageService.storeFile(imageBytes, fileName);  // 파일 저장 후 URL 반환

        // 이미지 URL 설정
        recognitionResult.setImageUrl(imageUrl);
        RecognitionResult savedResult = recognitionResultRepository.save(recognitionResult);

        // WebSocket을 통해 클라이언트로 알림 전송
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String resultJson = objectMapper.writeValueAsString(savedResult);
            System.out.println("Sending WebSocket message: " + resultJson); // 추가된 로그
            webSocketHandler.sendMessageToAll(resultJson);
            System.out.println("WebSocket message sent successfully"); // 추가된 로그
        } catch (Exception e) {
            e.printStackTrace();
        }

        return savedResult;
        // 파일 저장
//        String fileName = "image_" + System.currentTimeMillis() + ".jpg";
//        String imageUrl = "/uploads/" + fileName;  // 절대 경로로 설정
//        String filePath = "src/main/resources/static" + imageUrl;  // 실제 파일 시스템 경로
//
//        try {
//            java.nio.file.Path path = Paths.get(filePath);
//            Files.createDirectories(path.getParent());  // 디렉토리 생성
//            Files.write(path, imageBytes, StandardOpenOption.CREATE_NEW);
//        } catch (IOException e) {
//            throw new RuntimeException("저장 실패 " + fileName, e);
//        }
//
//        // 이미지 URL 설정
//        recognitionResult.setImageUrl(imageUrl);
//        RecognitionResult savedResult = recognitionResultRepository.save(recognitionResult);
//
//        // WebSocket을 통해 클라이언트로 알림 전송
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            String resultJson = objectMapper.writeValueAsString(savedResult);
//            System.out.println("Sending WebSocket message: " + resultJson);
//            webSocketHandler.sendMessageToAll(resultJson);
//            System.out.println("WebSocket message sent successfully"); // 로그 추가
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return savedResult;
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
