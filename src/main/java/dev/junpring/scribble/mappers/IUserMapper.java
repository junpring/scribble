package dev.junpring.scribble.mappers;

import dev.junpring.scribble.entities.member.UserEmailVerifyCodeEntity;
import dev.junpring.scribble.entities.member.UserEntity;
import dev.junpring.scribble.entities.member.SessionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;



@Mapper
public interface IUserMapper {

    int insertUserEmailVerificationCode(UserEmailVerifyCodeEntity userEmailVerifyCodeEntity);
    // 세션 생성
    int insertSession(SessionEntity sessionEntity);

    // 회원가입
    int insertUser(UserEntity userEntity);

    // 중복확인 COUNT(0)
    int selectUserCountByEmail(@Param(value = "email") String email);
    int selectUserCountByNickname(@Param(value = "nickname") String nickname);
    int selectUserCountByContact(@Param(value = "contact") String contact);

    // 로그인
    UserEntity selectUser(UserEntity userEntity);
    UserEntity selectUserEmail(
            @Param(value = "email")String email);
    SessionEntity selectSessionKey(
            @Param(value = "key") String key);
    // session key가 같으면 (세션 업데이트시간, 세션 만료될시간, 세션 만료된 여부) 업데이트.

    UserEmailVerifyCodeEntity selectUserEmailVerificationCode(
            @Param(value = "code") String code,
            @Param(value = "salt") String salt);
    UserEntity selectUserById(@Param(value = "id") int id);

    int updateUser(UserEntity userEntity);
    int updateUserEmailVerifyCode(UserEmailVerifyCodeEntity userEmailVerifyCodeEntity);
    int updateSession(SessionEntity sessionEntity);

    // 세션 만료된 여부
    int updateSessionExpired(
            @Param(value = "id") int id);
}
