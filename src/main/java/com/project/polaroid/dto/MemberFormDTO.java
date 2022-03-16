package com.project.polaroid.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Data
public class MemberFormDTO {
    private String memberEmail;
    private String memberPassword;
    private String memberNickname;
}
