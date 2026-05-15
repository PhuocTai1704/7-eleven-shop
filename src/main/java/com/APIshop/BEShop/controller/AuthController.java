package com.APIshop.BEShop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.APIshop.BEShop.payloads.dto.user.UserDTO;
import com.APIshop.BEShop.payloads.request.RequestLogin;
import com.APIshop.BEShop.payloads.request.RequestRegister;
import com.APIshop.BEShop.payloads.response.ResponseLogin;
import com.APIshop.BEShop.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/auth/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RequestRegister requestRegister) {

        UserDTO userDTO = authService.register(requestRegister);

        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ResponseLogin> login(@Valid @RequestBody RequestLogin requestLogin) {

        ResponseLogin responseLogin = authService.login(requestLogin);

        return ResponseEntity.ok(responseLogin);
    }
}
