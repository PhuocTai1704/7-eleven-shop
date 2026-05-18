package com.APIshop.BEShop.service;

import com.APIshop.BEShop.payloads.dto.user.UserDTO;
import com.APIshop.BEShop.payloads.request.RequestLogin;
import com.APIshop.BEShop.payloads.request.RequestRegister;
import com.APIshop.BEShop.payloads.response.ResponseLogin;

public interface AuthService {
    ResponseLogin login(RequestLogin requestLogin);

    UserDTO register(RequestRegister requestRegister);

    UserDTO getInfoByToken();

}
