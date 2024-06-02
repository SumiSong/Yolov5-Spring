package com.example.yolov5Project.security.service;

import com.example.yolov5Project.security.entity.UserEntity;
import com.example.yolov5Project.security.entity.UserPermission;
import com.example.yolov5Project.security.repository.UserInfoRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserSecurityService implements UserDetailsService {
    private final UserInfoRepository userInfoRepository;

    public UserSecurityService(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = this.userInfoRepository.findByUserId(userId);
        if(userEntity.isEmpty()){
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다");
        }
        UserEntity _userEntity = userEntity.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        if("admin".equals(userId)){ //admin으로 들어왔을 때
            authorities.add(new SimpleGrantedAuthority(UserPermission.ADMIN.getValue())); //어드민 권한 부여
        } else{
            authorities.add(new SimpleGrantedAuthority(UserPermission.User.getValue())); //유저 권한 부여
        }
        return new User(_userEntity.getUserId(), _userEntity.getUserPass(), authorities); //메소드 확인 유의
    }


}
