package com.APIshop.BEShop.service.impl;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.APIshop.BEShop.config.AppConstants;
import com.APIshop.BEShop.entity.Role;
import com.APIshop.BEShop.entity.User;
import com.APIshop.BEShop.exceptions.APIException;
import com.APIshop.BEShop.exceptions.ResourceNotFoundException;
import com.APIshop.BEShop.payloads.dto.user.UserDTO;
import com.APIshop.BEShop.payloads.request.RequestLogin;
import com.APIshop.BEShop.payloads.request.RequestRegister;
import com.APIshop.BEShop.payloads.response.ResponseLogin;
import com.APIshop.BEShop.repository.RoleRepo;
import com.APIshop.BEShop.repository.UserRepo;
import com.APIshop.BEShop.security.JWTUtil;
import com.APIshop.BEShop.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    private final JWTUtil jwtUtil;

    private final UserRepo userRepo;

    private final RoleRepo roleRepo;

    @Override
    public ResponseLogin login(RequestLogin requestLogin) {
        User user = userRepo.findByUsername(requestLogin.getUsername())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Tài khoản", "tên đăng nhập", requestLogin.getUsername())); // check

        // check password
        validateUserPassword(user, requestLogin);

        // Generate access token
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        String accessToken = jwtUtil.generateToken(userDTO);

        ResponseLogin responseLogin = new ResponseLogin(accessToken);
        return responseLogin;
    }

    @Override
    public UserDTO register(RequestRegister requestRegister) {
        if (userRepo.existsByUsername(requestRegister.getUsername())) {
            throw new APIException("Tên đăng nhập đã tồn tại");
        }
        Role role = roleRepo.findById(AppConstants.USER_ID).orElseThrow(
                () -> new ResourceNotFoundException("Role", "Id", AppConstants.USER_ID));
        User user = new User();
        String encodedPass = passwordEncoder.encode(requestRegister.getPassword());
        user.setFullName(requestRegister.getFullName());
        user.setUsername(requestRegister.getUsername());
        user.setPassword(encodedPass);
        user.setCreatedAt(LocalDateTime.now());
        user.getRoles().add(role);

        userRepo.save(user);

        return modelMapper.map(user, UserDTO.class);
    }

    private void validateUserPassword(User user, RequestLogin requestLogin) {
        boolean authentication = passwordEncoder.matches(requestLogin.getPassword(), user.getPassword());
        if (!authentication) {
            log.warn("Đăng nhập thất bại cho user: {}", requestLogin.getUsername());
            throw new APIException("Tên đăng nhập hoặc mật khẩu không đúng");
        }
    }
}
