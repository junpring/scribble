package dev.junpring.scribble.entities.board;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ReplyEntity {
    private int id;
    private int userId;
    private int articleId;
    private int replyId;
    private String content;
    private Date writtenAt;
}
