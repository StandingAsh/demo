package com.project.demo.domain.members.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MemberResponse {

    private Long id;
    private String name;
    private String userId;
    private String password;
    private String email;

    @Builder
    public MemberResponse(String name, String userId, String password, String email) {

        this.name = name;
        this.userId = userId;
        this.password = password;
        this.email = email;
    }
}
