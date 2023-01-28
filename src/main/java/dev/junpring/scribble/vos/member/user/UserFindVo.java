package dev.junpring.scribble.vos.member.user;

import dev.junpring.scribble.entities.member.UserEntity;
import dev.junpring.scribble.enums.member.user.FindResult;
import dev.junpring.scribble.interfaces.IResult;

public class UserFindVo extends UserEntity implements IResult<FindResult> {
    private FindResult result;

    public FindResult getResult() {
        return result;
    }

    public void setResult(FindResult result) {
        this.result = result;
    }
}
