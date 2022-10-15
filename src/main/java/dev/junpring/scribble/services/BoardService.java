package dev.junpring.scribble.services;

import dev.junpring.scribble.dtos.ArticleListDto;
import dev.junpring.scribble.dtos.ArticleReplyDTO;
import dev.junpring.scribble.dtos.SearchDto;
import dev.junpring.scribble.entities.board.*;
import dev.junpring.scribble.enums.board.*;
import dev.junpring.scribble.mappers.IBoardMapper;
import dev.junpring.scribble.vos.board.PaginationVo;
import dev.junpring.scribble.vos.board.article.ArticleWriteVo;
import dev.junpring.scribble.vos.board.article.ArticleListVo;
import dev.junpring.scribble.vos.board.BoardIdVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value = "dev.junpring.scribble.services.BoardService")
public class BoardService {

    private final IBoardMapper boardMapper;

    @Autowired
    public BoardService(IBoardMapper mapper) {
        this.boardMapper = mapper;
    }



    public void removeArticle(ArticleListVo articleListVo) {
        this.boardMapper.deleteArticleList(articleListVo);
        articleListVo.setResult(ArticleListResult.SUCCESS);

    }

    public void modifyArticle(ArticleListVo articleVo) {
        if (this.boardMapper.updateArticle(articleVo) > 0) {
            articleVo.setResult(ArticleListResult.SUCCESS);
        }
    }

    public void writeArticle(ArticleWriteVo writeVo) {
        if (this.boardMapper.insertArticleList(writeVo) == 0) {
            writeVo.setResult(ArticleWriteResult.FAILURE);
            return;
        }
        if (writeVo.getBoardCode() == null) {
            writeVo.setResult(ArticleWriteResult.ILLEGAL);
            return;
        }
        writeVo.setResult(ArticleWriteResult.SUCCESS); // 성공
    }

    // 가변인자. 여러 개의 매개변수를 받을 수 있다는 말
    public void uploadImages(ImageEntity... imageEntities) {
        for (ImageEntity imageEntity : imageEntities) {
            this.boardMapper.insertImage(imageEntity);
        }
    }

    public ImageEntity getImage(String id) {
        return this.boardMapper.selectImage(id);
    }
    public void getArticle(ArticleListDto articleListDto) {
        ArticleEntity articleEntity = this.boardMapper.selectForPrintArticle(articleListDto.getId());
        articleListDto.setBoardCode(articleEntity.getBoardCode());
        articleListDto.setTitle(articleEntity.getTitle());
        articleListDto.setUserId(articleEntity.getUserId());
        articleListDto.setContent(articleEntity.getContent());
        articleListDto.setWrittenAt(articleEntity.getWrittenAt());
        articleListDto.setView(articleEntity.getView());
        articleListDto.setExtra(articleEntity.getExtra());
    }

    public void getForPrintArticle(ArticleListVo articleListVo, int actorUserId) {
        ArticleEntity articleEntity = this.boardMapper.selectForPrintArticle(articleListVo.getId());
        if (articleEntity == null || articleEntity.getId() == 0) {
            articleListVo.setResult(ArticleListResult.FAILURE);
            return;
        }
        if (articleEntity.getUserId() == actorUserId) {
            articleListVo.setResultCode("S-1");
            articleListVo.setMsg("권한이 있습니다.");
        } else {
            articleListVo.setResultCode("F-1");
            articleListVo.setMsg("권한이 없습니다.");
        }

        articleListVo.setBoardCode(articleEntity.getBoardCode());
        articleListVo.setUserId(articleEntity.getUserId());
        articleListVo.setTitle(articleEntity.getTitle());
        articleListVo.setContent(articleEntity.getContent());
        articleListVo.setLike(articleEntity.getLike());
        articleListVo.setReplyCount(articleEntity.getReplyCount());
        articleListVo.setView(articleEntity.getView());
        articleListVo.setExtra(articleEntity.getExtra());
        articleListVo.setWrittenAt(articleEntity.getWrittenAt());

//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
//        //원하는 데이터 포맷 지정
//        String strNowDate = simpleDateFormat.format(articleEntity.getWrittenAt());
//        //지정한 포맷으로 변환
//        System.out.println("포맷 지정 후 : " + strNowDate);
        articleListVo.setResult(ArticleListResult.SUCCESS);
    }

    public PagingResponse<ArticleEntity> getArticlesForBoardList(final SearchDto params) {
        int count = this.boardMapper.selectArticlesCount(params);
        PaginationVo paginationVo = new PaginationVo(count, params);
        params.setPaginationVo(paginationVo);

        List<ArticleEntity> list = this.boardMapper.selectArticlesForBoardList(params);
        return new PagingResponse<>(list, paginationVo);
    }

    public List<ArticleListVo> rootArticleList(BoardEntity boardIdEntity) {
        return this.boardMapper.selectRootArticleList(boardIdEntity);
    }

    public List<BoardEntity> boardList() {
        return this.boardMapper.selectBoardCode();
    }

    public void boardIdList(BoardIdVo boardIdVo) {
        BoardEntity boardIdEntity = this.boardMapper.selectBoardByCode(boardIdVo);
        if (boardIdEntity == null || boardIdEntity.getId() == 0) {
            boardIdVo.setResult(BoardIdResult.NOT_FOUND);
            return;
        }
        boardIdVo.setName(boardIdEntity.getName());
        boardIdVo.setResult(BoardIdResult.SUCCESS);
    }


    public List<ArticleReplyDTO> getForPrintArticleReplies(int articleId) {
        return this.boardMapper.getForPrintArticleRepliesFrom(articleId);
    }

    public void writeReply(ArticleReplyDTO articleReplyDTO) {
        if (this.boardMapper.writeArticleReply(articleReplyDTO) > 0) {
            articleReplyDTO.setResultCode("S-1");
            articleReplyDTO.setMsg(String.format("%d번 게시글에 댓글이 생성되었습니다.", articleReplyDTO.getArticleId()));
        }
    }

    public ArticleReplyDTO getArticleModifyReplyAvailable(int id, int actorUserId) {
        ArticleReplyDTO articleReplyDto = this.boardMapper.selectArticleReply(id);
        if (articleReplyDto.getUserId() == actorUserId) {
            articleReplyDto.setResultCode("S-1");
            articleReplyDto.setMsg("수정권한이 있습니다.");
        } else {
            articleReplyDto.setResultCode("F-1");
            articleReplyDto.setMsg("수정권한이 없습니다.");
        }
        return articleReplyDto;
    }

    public void modifyArticleReply(ArticleReplyDTO articleReplyDto) {
        if (this.boardMapper.updateArticleReply(articleReplyDto) > 0) {
            articleReplyDto.setResultCode("S-1");
            articleReplyDto.setMsg(String.format("%d번 댓글이 수정되었습니다.", articleReplyDto.getId()));
        }
    }

    public ArticleReplyDTO getArticleDeleteReplyAvailableRs(int id, int actorUserId) {
        ArticleReplyDTO articleReplyDto = getArticleModifyReplyAvailable(id, actorUserId);
        String msg = articleReplyDto.getMsg().replace("수정", "삭제");
        articleReplyDto.setMsg(msg);
        return articleReplyDto;
    }

    public void deleteArticleReply(ArticleReplyDTO articleReplyDto) {
        if (this.boardMapper.deleteArticleReply(articleReplyDto) > 0) {
            articleReplyDto.setResultCode("S-1");
            articleReplyDto.setMsg(String.format("%d번 댓글이 삭제되었습니다.", articleReplyDto.getId()));
        }
    }

    public void articleViewCount(int id) {
        this.boardMapper.updateViewCount(id);
    }
}
