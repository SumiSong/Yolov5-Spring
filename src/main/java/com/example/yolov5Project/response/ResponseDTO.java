package com.example.yolov5Project.response;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;


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

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
}

