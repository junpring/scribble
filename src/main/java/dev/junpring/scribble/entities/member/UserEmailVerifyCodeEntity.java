package dev.junpring.scribble.entities.member;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserEmailVerifyCodeEntity {
    private int id; // 이메일인증 인덱스번호
    private int userId; // 유저인덱스번호
    private Date createdAt; // 이메일인증 코드 전송한 시간
    private Date expiresAt; // 이메일인증 코드 만료될 시간
    private boolean isExpired; // 이메일인증 확인 여부
    private String code; // 이메일인증 코드
    private String salt; // 이메일인증 코드
}
