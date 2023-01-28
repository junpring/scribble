package dev.junpring.scribble.vos.member.user;

import dev.junpring.scribble.entities.member.UserEntity;
import dev.junpring.scribble.enums.member.user.UserModifyNicknameResult;
import dev.junpring.scribble.interfaces.IResult;

public class UserModifyNicknameVo extends UserEntity implements IResult<UserModifyNicknameResult> {
    private UserModifyNicknameResult result;

    @Override
    public UserModifyNicknameResult getResult() {
        return result;
    }

    @Override
    public void setResult(UserModifyNicknameResult result) {
        this.result = result;
    }
}
