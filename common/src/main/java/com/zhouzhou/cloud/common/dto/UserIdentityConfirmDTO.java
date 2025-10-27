package com.zhouzhou.cloud.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserIdentityConfirmDTO implements Serializable {

    private static final Long serialVersionUID = 6237846238764872361L;

    private Long userId;

    private String userName;

    private String userPassword;

    private String token;
}
