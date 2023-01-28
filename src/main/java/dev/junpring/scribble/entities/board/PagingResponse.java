package dev.junpring.scribble.entities.board;

import dev.junpring.scribble.vos.board.PaginationVo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Getter
public class PagingResponse <T> {
    private List<T> list = new ArrayList<>();
    private final PaginationVo paginationVo;


    public PagingResponse(List<T> list, PaginationVo paginationVo) {
        this.list = list;
        this.paginationVo = paginationVo;
    }
}
