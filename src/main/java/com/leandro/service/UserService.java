package com.leandro.service;

import com.leandro.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${spring.external.service.base-url}")
    private String basePath;

    private final RestTemplate restTemplate;

    public List<UserDTO> getUsers(){
        UserDTO[] response = restTemplate.getForObject(basePath+"/users", UserDTO[].class);
        return Arrays.asList(response);
    }
    public String saveUser(UserDTO user){
         restTemplate.postForObject(basePath+"/users",user, UserDTO.class);
        return "Usuer save ok {}" ;
    }
}
