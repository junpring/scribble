package dev.junpring.scribble.vos.member.user;


import dev.junpring.scribble.entities.member.UserEntity;
import dev.junpring.scribble.enums.member.user.RegisterResult;
import dev.junpring.scribble.interfaces.IResult;

public class UserRegisterVo extends UserEntity implements IResult<RegisterResult> {

    private RegisterResult result;

    public RegisterResult getResult() {
        return result;
    }

    public void setResult(RegisterResult result) {
        this.result = result;
    }
}
