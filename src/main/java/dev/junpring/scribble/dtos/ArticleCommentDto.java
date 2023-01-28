package dev.junpring.scribble.dtos;

import dev.junpring.scribble.entities.board.CommentEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ArticleCommentDto extends CommentEntity {
    private String msg;
    private String resultCode;
    private String redirectUrl;

    private Map<String , Object> extra;
}
