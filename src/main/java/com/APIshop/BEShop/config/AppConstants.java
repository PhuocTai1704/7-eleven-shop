package com.APIshop.BEShop.config;

public class AppConstants {
    public static final String PAGE_NUMBER = "0";
    public static final String PAGE_SIZE = "5";
    public static final String SORT_BY = "id";
    public static final String SORT_DIR = "asc";
    public static final String SORT_USERS_BY = "userId";

    public static final Long ADMIN_ID = 101L;
    public static final Long USER_ID = 102L;
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
    public static final String[] PUBLIC_URLS = { "/v3/api-docs/**", "/swagger-ui/**", "/api/auth/register",
            "/api/auth/login",
    };
    public static final String[] USER_URLS = {};
    public static final String[] ADMIN_URLS = {};

}
