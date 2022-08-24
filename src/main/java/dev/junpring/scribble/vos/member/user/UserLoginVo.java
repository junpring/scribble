package dev.junpring.scribble.vos.member.user;

import dev.junpring.scribble.entities.member.UserEntity;
import dev.junpring.scribble.enums.member.user.LoginResult;
import dev.junpring.scribble.interfaces.IResult;
import dev.junpring.scribble.entities.member.SessionEntity;

public class UserLoginVo extends UserEntity implements IResult<LoginResult> {
    private LoginResult result;

    private boolean autosign;
    private SessionEntity sessionEntity; // SessionEntity 타입 (확장) , Vo는 이미 UserEntity를 상속받고잇음

    @Override
    public LoginResult getResult() {
        return result;
    }
    @Override
    public void setResult(LoginResult result) {
        this.result = result;
    }

    public boolean isAutosign() {
        return autosign;
    }

    public void setAutosign(boolean autosign) {
        this.autosign = autosign;
    }

    public SessionEntity getSessionEntity() {
        return sessionEntity;
    }

    public void setSessionEntity(SessionEntity sessionEntity) {
        this.sessionEntity = sessionEntity;
    }
}