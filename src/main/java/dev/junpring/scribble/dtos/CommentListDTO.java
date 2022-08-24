package dev.junpring.scribble.dtos;

import dev.junpring.scribble.entities.board.CommentEntity;

public class CommentListDTO extends CommentEntity {
    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
