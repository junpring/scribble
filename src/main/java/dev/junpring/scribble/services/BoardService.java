package dev.junpring.scribble.services;

import dev.junpring.scribble.dtos.ArticleCommentDto;
import dev.junpring.scribble.dtos.ArticleLikeDto;
import dev.junpring.scribble.dtos.ArticleListDto;
import dev.junpring.scribble.dtos.ArticleSearchDto;
import dev.junpring.scribble.entities.board.*;
import dev.junpring.scribble.enums.board.*;
import dev.junpring.scribble.mappers.IBoardMapper;
import dev.junpring.scribble.vos.board.PaginationVo;
import dev.junpring.scribble.vos.board.article.ArticleWriteVo;
import dev.junpring.scribble.vos.board.article.ArticleListVo;
import dev.junpring.scribble.vos.board.BoardIdVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
            System.out.println("FAILURE");
            writeVo.setResult(ArticleWriteResult.FAILURE);
            return;
        }
        if (writeVo.getBoardCode() == null) {
            System.out.println("ILLEGAL");
            writeVo.setResult(ArticleWriteResult.ILLEGAL);
            return;
        }
        writeVo.setResult(ArticleWriteResult.SUCCESS); // 성공
    }

    // 가변인자. 여러 개의 매개변수를 받을 수 있음
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

        if (articleEntity.getId() == 0 || articleEntity.isDeleted()) {
            articleListDto.setResultCode("F-1");
            articleListDto.setMsg("게시글이 존재하지 않거나 삭제된 게시물입니다.");
            return;
        }

        articleListDto.setResultCode("S-1");
        articleListDto.setMsg("게시글이 존재합니다.");
        articleListDto.setDeleted(articleEntity.isDeleted());
        articleListDto.setBoardCode(articleEntity.getBoardCode());
        articleListDto.setTitle(articleEntity.getTitle());
        articleListDto.setUserId(articleEntity.getUserId());
        articleListDto.setContent(articleEntity.getContent());
        articleListDto.setWrittenAt(articleEntity.getWrittenAt());
        articleListDto.setView(articleEntity.getView());
        articleListDto.setExtra(articleEntity.getExtra());
    }

    public void getForPrintArticle(ArticleListVo articleListVo, int connectedUserId) {
        ArticleEntity articleEntity = this.boardMapper.selectForPrintArticle(articleListVo.getId());
        if (articleEntity == null || articleEntity.getId() == 0) {
            articleListVo.setResult(ArticleListResult.FAILURE);
            return;
        }
        if (articleEntity.getUserId() == connectedUserId) {
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
        articleListVo.setCommentCount(articleEntity.getCommentCount());
        articleListVo.setView(articleEntity.getView());
        articleListVo.setExtra(articleEntity.getExtra());
        articleListVo.setWrittenAt(articleEntity.getWrittenAt());
        articleListVo.setResult(ArticleListResult.SUCCESS);

        /*
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        //원하는 데이터 포맷 지정
        String strNowDate = simpleDateFormat.format(articleEntity.getWrittenAt());
        //지정한 포맷으로 변환
        System.out.println("포맷 지정 후 : " + strNowDate);
        */
    }

    public PagingResponse<ArticleListDto> getArticlesForBoardList(ArticleSearchDto params) {
        int count = this.boardMapper.selectArticlesBoardListCount(params);
        PaginationVo paginationVo = new PaginationVo(count, params);
        params.setPaginationVo(paginationVo);


        List<ArticleListDto> list = this.boardMapper.selectArticlesForBoardList(params);
        return new PagingResponse<>(list, paginationVo);
    }

    public PagingResponse<ArticleListDto> getUserArticlesForBoardList(ArticleSearchDto params) {
        int count = this.boardMapper.selectUserArticlesCount(params);
        PaginationVo paginationVo = new PaginationVo(count, params);
        params.setPaginationVo(paginationVo);


        List<ArticleListDto> list = this.boardMapper.selectUserArticlesForList(params);
        return new PagingResponse<>(list, paginationVo);
    }

    public PagingResponse<ArticleListDto> getFindArticlesForList(ArticleSearchDto params) {
        int count = this.boardMapper.selectArticlesCount(params);
        PaginationVo paginationVo = new PaginationVo(count, params);
        params.setPaginationVo(paginationVo);

        List<ArticleListDto> list = this.boardMapper.selectFindArticlesForList(params);
        return new PagingResponse<>(list, paginationVo);
    }

    public List<ArticleEntity> rootArticleList(BoardEntity boardIdEntity) {
        return this.boardMapper.selectHomeArticleList(boardIdEntity);
    }

    public List<BoardEntity> getBoardList() {
        return this.boardMapper.selectBoardList();
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

    public List<ArticleCommentDto> getForPrintArticleReplies(int articleId) {
        return this.boardMapper.selectForPrintArticleRepliesFrom(articleId);
    }

    public void writeReply(ArticleCommentDto articleReplyDTO) {
        if (this.boardMapper.writeArticleReply(articleReplyDTO) > 0) {
            articleReplyDTO.setResultCode("S-1");
            articleReplyDTO.setMsg(String.format("%d번 게시글에 댓글이 생성되었습니다.", articleReplyDTO.getArticleId()));
        }
    }

    public ArticleCommentDto getArticleModifyReplyAvailable(int id, int connectedUserId) {
        ArticleCommentDto articleReplyDto = this.boardMapper.selectArticleReply(id);
        if (articleReplyDto.getUserId() == connectedUserId) {
            articleReplyDto.setResultCode("S-1");
            articleReplyDto.setMsg("수정권한이 있습니다.");
        } else {
            articleReplyDto.setResultCode("F-1");
            articleReplyDto.setMsg("수정권한이 없습니다.");
        }
        return articleReplyDto;
    }

    @Transactional
    public void modifyArticleReply(ArticleCommentDto articleCommentDto) {
        if (this.boardMapper.updateArticleReply(articleCommentDto) > 0) {
            articleCommentDto.setResultCode("S-1");
            articleCommentDto.setMsg(String.format("%d번 댓글이 수정되었습니다.", articleCommentDto.getId()));
        }
    }

    public ArticleCommentDto getArticleDeleteReplyAvailableRs(int id, int connectedUserId) {
        ArticleCommentDto articleCommentDto = getArticleModifyReplyAvailable(id, connectedUserId);
        String msg = articleCommentDto.getMsg().replace("수정", "삭제");
        articleCommentDto.setMsg(msg);
        return articleCommentDto;
    }

    @Transactional
    public void deleteArticleReply(ArticleCommentDto articleCommentDto) {
        if (this.boardMapper.deleteArticleReply(articleCommentDto) > 0) {
            articleCommentDto.setResultCode("S-1");
            articleCommentDto.setMsg(String.format("%d번 댓글이 삭제되었습니다.", articleCommentDto.getId()));
        }
    }

    public int articleViewCount(int id) {
        return this.boardMapper.updateViewCount(id);
    }

    public int articleCommentCount(int id) {
        return this.boardMapper.updateCommentCount(id);
    }

    public int articleLikeCount(int id) {
        return this.boardMapper.updateLikeCount(id);
    }

    public ArticleLikeDto getArticleLikeAvailable(int id, int connectedUserId) {
        ArticleEntity articleEntity = this.boardMapper.selectArticle(id);

        ArticleLikeDto articleLikeDto = new ArticleLikeDto();

        if (articleEntity.getUserId() == connectedUserId) {
            articleLikeDto.setResultCode("F-1");
            articleLikeDto.setMsg("본인은 추천 할 수 없습니다.");
            return articleLikeDto;
        }

        int likePoint = this.boardMapper.selectLikePointByUserId(id, connectedUserId);

        if (likePoint > 0) {
            articleLikeDto.setResultCode("F-2");
            articleLikeDto.setMsg("이미 좋아요를 하셨습니다.");
            return articleLikeDto;
        }

        articleLikeDto.setResultCode("S-1");
        articleLikeDto.setMsg("좋아요가 가능합니다.");
        return articleLikeDto;
    }

    public ArticleLikeDto addLikeArticle(int id, int connectedUserId) {
        ArticleLikeDto articleLikeDto = new ArticleLikeDto();
        if (this.boardMapper.insertLikeArticle(id, connectedUserId) > 0) {
            articleLikeDto.setResultCode("S-1");
            articleLikeDto.setMsg(String.format("%d번 게시물을 추천하였습니다.", id));
        }
        return articleLikeDto;
    }

    public int getLikePoint(int id) {
        return boardMapper.selectLikePoint(id);
    }

    public int getLikePointByUserId(int id, int connectedUserId) {
        return boardMapper.selectLikePointByUserId(id, connectedUserId);
    }

    public ArticleLikeDto getArticleCancelLikeAvailable(int id, int connectedUserId) {
        ArticleLikeDto articleLikeDto = new ArticleLikeDto();

        int likePoint = this.boardMapper.selectLikePointByUserId(id, connectedUserId);

        if (likePoint == 0) {
            articleLikeDto.setResultCode("F-1");
            articleLikeDto.setMsg("추천하신 분만 취소가 가능합니다.");
            return articleLikeDto;
        }

        articleLikeDto.setResultCode("S-1");
        articleLikeDto.setMsg("취소가 가능합니다");
        return articleLikeDto;
    }

    public ArticleLikeDto cancelLikeArticle(int id, int connectedUserId) {
        this.boardMapper.deleteCancelLikeArticle(id, connectedUserId);
        ArticleLikeDto articleLikeDto = new ArticleLikeDto();

        articleLikeDto.setResultCode("S-1");
        articleLikeDto.setMsg(String.format("%d번 게시물을 추천을 취소하였습니다.", id));
        return articleLikeDto;
    }

    public List<ArticleListDto> getForPrintRcmdArticles() {
        return this.boardMapper.selectRcmdArticles();
    }


}
