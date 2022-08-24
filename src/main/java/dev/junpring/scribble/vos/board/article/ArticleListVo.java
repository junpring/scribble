package dev.junpring.scribble.vos.board.article;

import dev.junpring.scribble.entities.board.ArticleEntity;
import dev.junpring.scribble.enums.board.ArticleListResult;
import dev.junpring.scribble.interfaces.IResult;

public class ArticleListVo extends ArticleEntity implements IResult<ArticleListResult> {
    private ArticleListResult result;
    private int articleIndex;
    private String name;

    public int getArticleIndex() {
        return articleIndex;
    }
    public void setArticleIndex(int articleIndex) {
        this.articleIndex = articleIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
