package dev.junpring.scribble.entities.member;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SessionEntity {
    private int id; // 인덱스번호
    private Date createdAt; // 세션 생성시간
    private Date updatedAt; // 세션 업데이트 시간 , (로그인 연장)
    private Date expiresAt; // 세션 만료될 시간
    private boolean isExpired; // 세션 만료된 여부
    private boolean isKeepLoggedIn; // 세션 만료된 여부
    private int userId; // 유저 인덱스 번호
    private String key; //
    private String ua; //


}
