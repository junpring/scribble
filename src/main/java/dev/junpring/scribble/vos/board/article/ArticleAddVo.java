package dev.junpring.scribble.vos.board.article;

import dev.junpring.scribble.entities.board.ArticleEntity;
import dev.junpring.scribble.enums.board.ArticleAddResult;
import dev.junpring.scribble.interfaces.IResult;

public class ArticleAddVo extends ArticleEntity implements IResult<ArticleAddResult> {
    private ArticleAddResult result;

    @Override
    public ArticleAddResult getResult() {
        return result;
    }

    @Override
    public void setResult(ArticleAddResult result) {
        this.result = result;
    }
}
