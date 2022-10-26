package dev.junpring.scribble.vos.board.article;

import dev.junpring.scribble.entities.board.ArticleEntity;
import dev.junpring.scribble.enums.board.ArticleListResult;
import dev.junpring.scribble.interfaces.IResult;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ArticleListVo extends ArticleEntity implements IResult<ArticleListResult> {
    private ArticleListResult result;

    private String msg;
    private String resultCode;
    private Map<String, Object> extra;

//  articleId 이거 방법이 있을거임 루트페이지와 함께 수정해야됨 !


    @Override
    public Map<String, Object> getExtra() {
        return extra;
    }

    @Override
    public void setExtra(Map<String, Object> extra) {
        this.extra = extra;
    }

    @Override
    public ArticleListResult getResult() {
        return result;
    }

    @Override
    public void setResult(ArticleListResult result) {
        this.result = result;
    }
}
