package com.APIshop.BEShop.config;

import java.security.Principal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoConfig implements Principal {

    private static final long serialVersionUID = 1L;

    private String userId;

    private List<String> roles;

    @Override
    public String getName() {
        return this.userId;
    }
}