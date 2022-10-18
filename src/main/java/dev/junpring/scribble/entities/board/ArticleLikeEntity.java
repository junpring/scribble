package dev.junpring.scribble.entities.board;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ArticleLikeEntity {
    private int id;
    private Date regDate;
    private int articleId;
    private int userId;
    private int point;

}
