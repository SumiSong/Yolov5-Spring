package com.example.yolov5Project.security.service;

import com.example.yolov5Project.security.DTO.DTORegister;
import com.example.yolov5Project.security.entity.UserEntity;
import com.example.yolov5Project.security.repository.UserInfoRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class JoinService {
    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;

    public JoinService(UserInfoRepository userInfoRepository, PasswordEncoder passwordEncoder) {
        this.userInfoRepository = userInfoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void join(DTORegister dtoRegister) {
        //dto->entity 변환
        UserEntity userEntity = convertDtoEntity(dtoRegister);
        userInfoRepository.save(userEntity);
    }

    private UserEntity convertDtoEntity(DTORegister dtoRegister){ //엔티티 필드에 각 저장하기
        UserEntity userEntity = new UserEntity();
        userEntity.setUserCode(dtoRegister.getUserCode());
        userEntity.setUserId(dtoRegister.getUserId());
        // 객체를 직접 생성하지 않고 빈으로 등록된 Password Encoder 객체를 주입하여 사용
        userEntity.setUserPass(passwordEncoder.encode(dtoRegister.getUserPass()));
        userEntity.setUserName(dtoRegister.getUserName());
        userEntity.setUserEmail(dtoRegister.getUserEmail());
        return userEntity;
    }
}
