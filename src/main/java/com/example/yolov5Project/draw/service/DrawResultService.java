package com.example.yolov5Project.draw.service;

import com.example.yolov5Project.draw.entity.DrawResult;
import com.example.yolov5Project.draw.repository.DrawResultRepository;
import com.example.yolov5Project.study.entity.RecognitionResult;
import com.example.yolov5Project.websocket.WebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DrawResultService {
    private final DrawResultRepository drawResultRepository;
    private final DrawFileStorageService drawFileStorageService;
    private final WebSocketHandler webSocketHandler;

    @Autowired

    public DrawResultService(DrawResultRepository drawResultRepository, DrawFileStorageService drawFileStorageService, WebSocketHandler webSocketHandler, DrawResultRepository drawResultRepository1, DrawFileStorageService drawFileStorageService1, WebSocketHandler webSocketHandler1) {
        this.drawResultRepository = drawResultRepository;
        this.drawFileStorageService = drawFileStorageService;
        this.webSocketHandler = webSocketHandler;
    }

    public DrawResult saveResult(DrawResult drawResult, byte[] imageBytes) {
        String fileName = "image_" + System.currentTimeMillis() + ".jpg";
        String imageUrl = drawFileStorageService.storeFile(imageBytes, fileName);  // 파일 저장 후 URL 반환

        // 이미지 URL 설정
        drawResult.setDrawimageUrl(imageUrl);
        DrawResult savedResult = drawResultRepository.save(drawResult);

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
    }

    //모든 결과 조회
    public List<DrawResult> getAllResults() {
        return drawResultRepository.findAll();
    }

    //세부 항목 조회
    public DrawResult getResultById(Long id) {
        return drawResultRepository.findById(id).orElse(null);
    }

    // 삭제
    public void deleteResultById(Long id) {
        if (drawResultRepository.existsById(id)) {
            drawResultRepository.deleteById(id);
            System.out.println("Deleted result with ID: " + id); // 로그 추가
        } else {
            throw new NoSuchElementException("Result not found with id: " + id);
        }
    }

}
