package dev.junpring.scribble.entities.member;

import java.util.Date;

public class UserEntity{
    private int index; // 유저 인덱스 번호
    private String email; // 유저 email, id
    private String password; // 유저 password
    private String nickname; // 유저 이름
    private String contact; // 유저 번호
    private int level; // 게시판등 볼 수 잇는 유저 권한
    private String addressPostal; // 유저 우편번호
    private String addressPrimary; //유저 주소
    private String addressSecondary; // 유저 상세주소
    private boolean isEmailVerified; // 유저 이메일 인증여부
    private Date registeredAt; // 유저 생성시간
    private Date modifiedAt; // 유저 수정시간
    private boolean isDeleted; // 계정삭제
    private boolean isSuspended; // 계정정지

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getAddressPostal() {
        return addressPostal;
    }

    public void setAddressPostal(String addressPostal) {
        this.addressPostal = addressPostal;
    }

    public String getAddressPrimary() {
        return addressPrimary;
    }

    public void setAddressPrimary(String addressPrimary) {
        this.addressPrimary = addressPrimary;
    }

    public String getAddressSecondary() {
        return addressSecondary;
    }

    public void setAddressSecondary(String addressSecondary) {
        this.addressSecondary = addressSecondary;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public Date getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(Date registeredAt) {
        this.registeredAt = registeredAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isSuspended() {
        return isSuspended;
    }

    public void setSuspended(boolean suspended) {
        isSuspended = suspended;
    }
}
