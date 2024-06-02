package com.example.yolov5Project.response;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@ToString
public class ResponseDTO {
        private int code;
        private String msg;
        private Object data;
        public ResponseDTO (HttpStatus httpStatus, String msg, Object data){
            this.code = httpStatus.value();
            this.msg = msg;
            this.data = data;
        }
    }

