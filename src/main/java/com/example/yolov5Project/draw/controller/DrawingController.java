package com.example.yolov5Project.draw.controller;
import com.example.yolov5Project.draw.entity.DrawResult;
import com.example.yolov5Project.draw.service.DrawResultService;
import com.example.yolov5Project.response.ResponseDTO;
import com.example.yolov5Project.response.Tool;
import com.example.yolov5Project.study.entity.RecognitionResult;
import com.example.yolov5Project.websocket.WebSocketHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


@Controller
@RequestMapping("/api/draw")
public class DrawingController {

    private final Tool tool;
    private final DrawResultService drawResultService;
    private final WebSocketHandler webSocketHandler;

    public DrawingController(Tool tool, DrawResultService drawResultService, WebSocketHandler webSocketHandler) {
        this.tool = tool;
        this.drawResultService = drawResultService;
        this.webSocketHandler = webSocketHandler;
    }

    @GetMapping("draw")
        public String drawingHome(Model model) {
        List<DrawResult> results = drawResultService.getAllResults();
        model.addAttribute("results", results);
        return "drawingCanvas";
    }

    @GetMapping("/results/{id}")
    public String getResultById(@PathVariable Long id, Model model) {
        DrawResult result = drawResultService.getResultById(id);
        if (result == null) {
            return "error/404";
        }
        model.addAttribute("result", result);
        return "canvasResultDetail";
    }

    @DeleteMapping("/results/{id}")
    public ResponseEntity<ResponseDTO> deleteResult(@PathVariable Long id) {
        System.out.println("DELETE request received for ID: " + id); // 로그 추가
        try {
            drawResultService.deleteResultById(id);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/predict")
    @ResponseBody
    public ResponseEntity<ResponseDTO> predict(@RequestBody Map<String, String> payload) {
        String base64Image = payload.get("image");

        // Base64 데이터 디코딩
        String base64Data = base64Image.split(",")[1]; // data:image/jpeg;base64, 제거
        byte[] imageBytes = Base64.getDecoder().decode(base64Data);

        // Flask 서버로 이미지 전송
        String flaskUrl = "http://localhost:5000/predict";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        ByteArrayResource imageResource = new ByteArrayResource(imageBytes) {
            @Override
            public String getFilename() {
                return "image.jpg";
            }
        };
        body.add("file", imageResource);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response;
        try {
            response = restTemplate.postForEntity(flaskUrl, requestEntity, String.class);
            // JSON 응답 파싱
            String jsonResponse = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> resultList = objectMapper.readValue(jsonResponse, new TypeReference<List<Map<String, Object>>>() {});

            // 예측 결과 저장
            for (Map<String, Object> result : resultList) {
                DrawResult drawResult = new DrawResult();
                drawResult.setDrawobjectName(result.get("class").toString());
                drawResult.setDrawconfidence(Double.parseDouble(result.get("confidence").toString()));
                drawResultService.saveResult(drawResult, imageBytes);
            }

            // WebSocket을 통해 클라이언트로 결과 전송
            webSocketHandler.sendMessageToAll(jsonResponse);

            ResponseDTO responseDTO = new ResponseDTO(HttpStatus.OK, "예측 결과", resultList);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return tool.res(HttpStatus.INTERNAL_SERVER_ERROR, "Flask API 호출 에러 발생: " + e.getMessage(), null);
        }
    }
}
