package dev.junpring.scribble.entities.board;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CommentEntity {
    private int id;
    private int userId;
    private int articleId;
    private int replyId; // 몇번 id의 대댓글인가
//    private int replyDepth; // 0은 일반댓글 ,, 1은 대댓글.
//    private int replyOrder; // 순서
    private String content;
    private Date writtenAt;
    private Date modifiedAt;
    private boolean isDeleted;
}
