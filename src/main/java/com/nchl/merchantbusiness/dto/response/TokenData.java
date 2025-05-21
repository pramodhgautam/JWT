package com.nchl.merchantbusiness.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenData {

    private String accessToken;
    private String tokenType;
    private String refreshToken;
    private Integer expiresIn;
}
