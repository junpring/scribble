package dev.junpring.scribble.entities.member;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserEntity{
//    public static final String ATTRIBUTE_NAME = "userEntity";
    private int id; // 유저 인덱스 번호
    private String email; // 유저 email, id
    private String password; // 유저 password
    private String nickname; // 유저 이름
    private String contact; // 유저 번호
    private int level; // 게시판등 볼 수 잇는 유저 권한
    private String levelText;
    private String addressPostal; // 유저 우편번호
    private String addressPrimary; //유저 주소
    private String addressSecondary; // 유저 상세주소
    private boolean isEmailVerified; // 유저 이메일 인증여부
    private Date registeredAt; // 유저 생성시간
    private Date modifiedAt; // 유저 수정시간
    private Date modifyExpiresAt; // 수정 가능한 시간
    private boolean isDeleted; // 계정삭제
    private boolean isSuspended; // 계정정지

}
