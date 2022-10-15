package dev.junpring.scribble.dtos;

import dev.junpring.scribble.entities.board.ArticleEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ArticleListDto extends ArticleEntity {
    private String msg;
    private String resultCode;
    private int articleId;

    private Map<String, Object> extra;
}
