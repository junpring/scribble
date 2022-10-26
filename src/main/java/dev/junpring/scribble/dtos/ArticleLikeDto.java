package dev.junpring.scribble.dtos;

import dev.junpring.scribble.entities.board.ArticleLikeEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleLikeDto extends ArticleLikeEntity {
    private String resultCode;
    private String msg;
    private int likePoint;
    private int likePointByUserId;
}
