package dev.junpring.scribble.entities.board;

public class BoardIdEntity {
    private int index;
    private String id; // 게시판 명칭(id)
    private String name; // 게시판 이름
    private int listLevel; // 게시글 읽을 수 있는 권한 레벨
    private int readLevel; // 게시글 자세히 읽을 수 있는 권한 레벨
    private int writeLevel; // 게시글 작성할 수 있는 권한 레벨
    private int commentLevel; // 댓글 작성할 수 있는 권한 레벨

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
