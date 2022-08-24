package dev.junpring.scribble.vos.member.user;

import dev.junpring.scribble.interfaces.IResult;
import dev.junpring.scribble.entities.member.UserEmailVerifyCodeEntity;
import dev.junpring.scribble.enums.member.user.UserEmailVerifyResult;

public class UserEmailVerifyCodeVo extends UserEmailVerifyCodeEntity implements IResult<UserEmailVerifyResult> {
    private UserEmailVerifyResult result;

    @Override
    public UserEmailVerifyResult getResult() {
        return result;
    }

    @Override
    public void setResult(UserEmailVerifyResult result) {
        this.result = result;
    }
}
