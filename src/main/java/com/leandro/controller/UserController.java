package com.leandro.controller;


import com.leandro.dto.UserDTO;
import com.leandro.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll(){
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id){
        try {
            UserDTO user = userService.getUserById(id);
            return ResponseEntity.ok(user); // Devuelve el usuario con estado 200 OK si lo encuentra
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // Devuelve un 404 si el usuario no se encuentra
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Devuelve un 500 si ocurre algún error
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String save(@RequestBody UserDTO user){
        userService.saveUser(user);
        return "Usuario creado ok";
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updataUser(@PathVariable Long id, @RequestBody UserDTO user){
        userService.updatUser(id, user);
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);

        if (deleted) {
           return ResponseEntity.ok("El id " + id + " se borró correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se pudo borrar el id " + id + ", ya que no existe.");
        }

    }


}
