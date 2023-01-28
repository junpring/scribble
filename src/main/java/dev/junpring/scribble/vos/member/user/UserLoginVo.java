package dev.junpring.scribble.vos.member.user;

import dev.junpring.scribble.entities.member.UserEntity;
import dev.junpring.scribble.enums.member.user.LoginResult;
import dev.junpring.scribble.interfaces.IResult;
import dev.junpring.scribble.entities.member.SessionEntity;

public class UserLoginVo extends UserEntity implements IResult<LoginResult> {
    private LoginResult result;

    private boolean autologin;
    private SessionEntity sessionEntity;

    @Override
    public LoginResult getResult() {
        return result;
    }
    @Override
    public void setResult(LoginResult result) {
        this.result = result;
    }

    public boolean isAutologin() {
        return autologin;
    }

    public void setAutologin(boolean autologin) {
        this.autologin = autologin;
    }

    public SessionEntity getSessionEntity() {
        return sessionEntity;
    }

    public void setSessionEntity(SessionEntity sessionEntity) {
        this.sessionEntity = sessionEntity;
    }
}