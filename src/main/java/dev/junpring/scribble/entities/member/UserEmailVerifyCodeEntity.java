package dev.junpring.scribble.entities.member;

import java.util.Date;

public class UserEmailVerifyCodeEntity {
    private int index; // 이메일인증 인덱스번호
    private int userIndex; // 유저인덱스번호
    private Date createdAt; // 이메일인증 코드 전송한 시간
    private Date expiresAt; // 이메일인증 코드 만료될 시간
    private boolean isExpired; // 이메일인증 확인 여부
    private String code; // 이메일인증 코드
    private String salt; // 이메일인증 코드

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getUserIndex() {
        return userIndex;
    }

    public void setUserIndex(int userIndex) {
        this.userIndex = userIndex;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
