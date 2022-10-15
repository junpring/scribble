package dev.junpring.scribble.entities.board;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardEntity {
    private int id;
    private String code; // 게시판 명칭(id)
    private String name; // 게시판 이름
    private int listLevel; // 게시글 읽을 수 있는 권한 레벨
    private int readLevel; // 게시글 자세히 읽을 수 있는 권한 레벨
    private int writeLevel; // 게시글 작성할 수 있는 권한 레벨
    private int commentLevel; // 댓글 작성할 수 있는 권한 레벨
}
