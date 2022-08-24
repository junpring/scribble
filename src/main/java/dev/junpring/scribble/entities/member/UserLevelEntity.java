package dev.junpring.scribble.entities.member;

public class UserLevelEntity {
    private int level; // 유저 권한
    private String text; // 유저 상태 (일반유저, 관리자 등)

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
