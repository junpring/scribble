package dev.junpring.scribble.entities.member;

public class UserBoardEntity {
    private int index; // index
    private String id; // 게시판 종류
    private String name; // 게시판 이름
    private int listLevel; // 게시판을 확인 할 수 있는 유저의 레벨
    private int readLevel; // 게시글을 읽을 수 있는 유저의 레벨
    private int writeLevel; // 게시글을 작성할 수 있는 유저의 레벨
    private int commentLevel; // 댓글을 작성할 수 있는 유저의 레벨

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getListLevel() {
        return listLevel;
    }

    public void setListLevel(int listLevel) {
        this.listLevel = listLevel;
    }

    public int getReadLevel() {
        return readLevel;
    }

    public void setReadLevel(int readLevel) {
        this.readLevel = readLevel;
    }

    public int getWriteLevel() {
        return writeLevel;
    }

    public void setWriteLevel(int writeLevel) {
        this.writeLevel = writeLevel;
    }

    public int getCommentLevel() {
        return commentLevel;
    }

    public void setCommentLevel(int commentLevel) {
        this.commentLevel = commentLevel;
    }
}
