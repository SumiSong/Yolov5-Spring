package com.example.yolov5Project.security.response;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {
        private int code;
        private String msg;
        private Object obj;
        public ResponseDTO (HttpStatus httpStatus, String msg, Object data){
            this.code = httpStatus.value();
            this.msg = msg;
            obj = data;
        }


}
