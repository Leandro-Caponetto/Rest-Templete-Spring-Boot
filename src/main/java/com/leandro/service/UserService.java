package com.leandro.service;

import com.leandro.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    @Value("${spring.external.service.base-url}")
    private String basePath;

    private final RestTemplate restTemplate;

    public List<UserDTO> getUsers(){
        UserDTO[] response = restTemplate.getForObject(basePath+"/users", UserDTO[].class);
        return Arrays.asList(response);
    }
    public UserDTO getUserById(Long id) {
        String url = String.format("%s/users/%d", basePath, id);

        try {
            return restTemplate.getForObject(url, UserDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            // Maneja cuando el usuario no se encuentra (404)
            log.error("Usuario con id {} no encontrado.", id);
            throw new RuntimeException("Usuario no encontrado");
        } catch (RestClientException e) {
            // Maneja otros errores relacionados con la comunicación HTTP
            log.error("Error al comunicarse con el servicio: {}", e.getMessage());
            throw new RuntimeException("Error al obtener el usuario");
        }
    }

    public String saveUser(UserDTO user){
         restTemplate.postForObject(basePath+"/users",user, UserDTO.class);
        return "Usuer save ok {}" ;
    }

    public void updatUser(Long id, UserDTO user){
        restTemplate.put(basePath+"/users/"+id, user);
    }

    public  boolean deleteUser(Long id){
        try {
            restTemplate.delete(basePath + "/users/" + id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
