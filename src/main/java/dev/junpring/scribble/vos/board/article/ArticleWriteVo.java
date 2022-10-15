package dev.junpring.scribble.vos.board.article;

import dev.junpring.scribble.entities.board.ArticleEntity;
import dev.junpring.scribble.enums.board.ArticleWriteResult;
import dev.junpring.scribble.interfaces.IResult;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleWriteVo extends ArticleEntity implements IResult<ArticleWriteResult> {
    private ArticleWriteResult result;
    private String msg;
    private String resultCode;
    private String redirectUrl;

    @Override
    public ArticleWriteResult getResult() {
        return result;
    }

    @Override
    public void setResult(ArticleWriteResult result) {
        this.result = result;
    }
}
