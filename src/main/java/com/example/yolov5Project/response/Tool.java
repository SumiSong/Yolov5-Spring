package com.example.yolov5Project.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class Tool {
    public ResponseEntity<ResponseDTO> res(HttpStatus code, String msg, Object data){
        return ResponseEntity.ok().body(new ResponseDTO(code, msg, data));
    }
    public ResponseEntity<ResponseDTO> resErr(String msg){
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.BAD_REQUEST,msg, null));
    }

    public ResponseEntity<ResponseDTO> resCustom(HttpStatus code, String msg, Object data){
        return ResponseEntity.ok().body(new ResponseDTO(code, msg, data));
    }
}
