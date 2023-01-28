package dev.junpring.scribble.vos.member.user;

import dev.junpring.scribble.entities.member.UserEntity;
import dev.junpring.scribble.enums.member.user.UserWithdrawalResult;
import dev.junpring.scribble.interfaces.IResult;

public class UserWithdrawalVo extends UserEntity implements IResult<UserWithdrawalResult> {
    private UserWithdrawalResult result;

    @Override
    public UserWithdrawalResult getResult() {
        return result;
    }

    @Override
    public void setResult(UserWithdrawalResult result) {
        this.result = result;
    }
}
