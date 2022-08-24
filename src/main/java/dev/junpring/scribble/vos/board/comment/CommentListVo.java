package dev.junpring.scribble.vos.board.comment;

import dev.junpring.scribble.dtos.CommentListDTO;
import dev.junpring.scribble.entities.board.CommentEntity;
import dev.junpring.scribble.enums.board.CommentListResult;
import dev.junpring.scribble.interfaces.IResult;

import java.util.List;

public class CommentListVo extends CommentEntity implements IResult<CommentListResult> {
    private CommentListResult result;
    private List<CommentListDTO> comments;


    public List<CommentListDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentListDTO> comments) {
        this.comments = comments;
    }

    @Override
    public CommentListResult getResult() {
        return result;
    }

    @Override
    public void setResult(CommentListResult result) {
        this.result = result;
    }
}
