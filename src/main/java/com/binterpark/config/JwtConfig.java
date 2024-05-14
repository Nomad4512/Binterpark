package com.binterpark.config;

import com.binterpark.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtConfig {

    @Value("${JWT_TOKEN}")
    private String secretKey;

    @Value("${JWT_EXPIRE_TIME}")
    private long expireTime;

    private final UserService userService;


}
