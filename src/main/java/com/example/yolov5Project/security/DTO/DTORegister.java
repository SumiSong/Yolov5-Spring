package com.example.yolov5Project.security.DTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DTORegister {

    private Long userCode;
    private String userName;
    private String userId;
    private String userPass;
    private String userEmail;

}
