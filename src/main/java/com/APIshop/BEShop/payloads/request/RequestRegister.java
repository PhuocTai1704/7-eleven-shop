package com.APIshop.BEShop.payloads.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestRegister {
    @NotBlank(message = "Tên tài khoản không được để trống")
    private String fullName;

    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Pattern(regexp = ".*[a-zA-Z]+.*", message = "Tên đăng nhập phải chứa ít nhất 1 chữ cái")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;

}