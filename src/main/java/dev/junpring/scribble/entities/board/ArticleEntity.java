package dev.junpring.scribble.entities.board;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
public class ArticleEntity {
    private int id;
    private String boardCode; // 게시판 명칭(id)
    private int userId;
    private String title; // 제목
    private String content; // 내용
    private int bestLevel; // 베스트 유무
    private int replyCount; //댓글 수
    private int view; // 조회수
    private Date writtenAt; // 글 작성한 시간
    private int boardId;

    private Map<String, Object> extra;

}
