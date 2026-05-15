package com.APIshop.BEShop.config;

import java.security.Principal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoConfig implements Principal {

    private static final long serialVersionUID = 1L;

    private String userId;

    private String role;

    @Override
    public String getName() {
        return this.userId;
    }
}