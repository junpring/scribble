package dev.junpring.scribble.dtos;

import dev.junpring.scribble.vos.board.PaginationVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchDto{
    private String boardCode;
    private int userId;
    private int page;             // 현재 페이지 번호
    private int recordSize;       // 페이지당 출력할 데이터 개수
    private int pageSize;         // 화면 하단에 출력할 페이지 사이즈
    private String keyword;       // 검색 키워드
    private String searchType;    // 검색 유형
    private PaginationVo paginationVo;    // 페이지네이션 정보

    public SearchDto() {
        this.page = 1;
        this.recordSize = 15;
        this.pageSize = 10;
    }
}
