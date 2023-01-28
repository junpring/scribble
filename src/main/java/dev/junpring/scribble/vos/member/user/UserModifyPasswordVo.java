package dev.junpring.scribble.vos.member.user;

import dev.junpring.scribble.entities.member.UserEntity;
import dev.junpring.scribble.enums.member.user.UserModifyPasswordResult;
import dev.junpring.scribble.interfaces.IResult;

public class UserModifyPasswordVo extends UserEntity implements IResult<UserModifyPasswordResult> {
    private String currentPasswordCheck;
    private UserModifyPasswordResult result;


    @Override
    public UserModifyPasswordResult getResult() {
        return result;
    }

    public void setResult(UserModifyPasswordResult result) {
        this.result = result;
    }

    public String getCurrentPasswordCheck() {
        return currentPasswordCheck;
    }

    public void setCurrentPasswordCheck(String currentPasswordCheck) {
        this.currentPasswordCheck = currentPasswordCheck;
    }
}
