package com.APIshop.BEShop.payloads.dto.user;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String userId;

    @Size(min = 5, max = 50, message = "Họ tên phải từ 5 đến 50 ký tự")
    private String fullName;
    private String username;
    private LocalDateTime createdAt;

    private Set<RoleDTO> roles = new HashSet<>();

}
