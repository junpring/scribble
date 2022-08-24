package dev.junpring.scribble.vos.board;

import dev.junpring.scribble.entities.board.BoardIdEntity;
import dev.junpring.scribble.enums.board.BoardIdResult;
import dev.junpring.scribble.interfaces.IResult;

public class BoardIdVo extends BoardIdEntity implements IResult<BoardIdResult> {
    private BoardIdResult result;

    @Override
    public BoardIdResult getResult() {
        return result;
    }

    @Override
    public void setResult(BoardIdResult result) {
        this.result = result;
    }
}
