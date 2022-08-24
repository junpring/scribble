package dev.junpring.scribble.vos.board.comment;

import dev.junpring.scribble.entities.board.CommentEntity;
import dev.junpring.scribble.enums.board.CommentAddResult;
import dev.junpring.scribble.interfaces.IResult;

public class CommentAddVo extends CommentEntity implements IResult<CommentAddResult> {
    private CommentAddResult result;

    @Override
    public CommentAddResult getResult() {
        return result;
    }

    @Override
    public void setResult(CommentAddResult result) {
        this.result = result;
    }
}
