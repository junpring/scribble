package dev.junpring.scribble.entities.board;

import java.util.Date;

public class nestedReplyEntity {
    private int index;
    private int user_index;
    private int comment_index;
    private Date written_at;
    private Date updated_at;
    private String content;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getUser_index() {
        return user_index;
    }

    public void setUser_index(int user_index) {
        this.user_index = user_index;
    }

    public int getComment_index() {
        return comment_index;
    }

    public void setComment_index(int comment_index) {
        this.comment_index = comment_index;
    }

    public Date getWritten_at() {
        return written_at;
    }

    public void setWritten_at(Date written_at) {
        this.written_at = written_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
