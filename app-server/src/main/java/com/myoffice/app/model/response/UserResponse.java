package com.myoffice.app.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {

    private String username;

    private String token;
}
