package dev.junpring.scribble.services;

import dev.junpring.scribble.dtos.CommentListDTO;
import dev.junpring.scribble.entities.board.ArticleEntity;
import dev.junpring.scribble.entities.board.BoardIdEntity;
import dev.junpring.scribble.entities.board.CommentEntity;
import dev.junpring.scribble.entities.board.ImageEntity;
import dev.junpring.scribble.enums.board.*;
import dev.junpring.scribble.mappers.IBoardMapper;
import dev.junpring.scribble.vos.board.article.ArticleAddVo;
import dev.junpring.scribble.vos.board.article.ArticleListVo;
import dev.junpring.scribble.vos.board.BoardIdVo;
import dev.junpring.scribble.vos.board.comment.CommentAddVo;
import dev.junpring.scribble.vos.board.comment.CommentListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service(value = "dev.junpring.scribble.services.BoardService")
public class BoardService {

    private final IBoardMapper boardMapper;

    @Autowired
    public BoardService(IBoardMapper mapper) {
        this.boardMapper = mapper;
    }
    public void removeArticle(ArticleEntity articleEntity) {
        this.boardMapper.deleteArticleList(articleEntity);
    }
    public void addArticle(ArticleAddVo addVo) {
        if (this.boardMapper.insertArticleList(addVo) == 0) {
            addVo.setResult(ArticleAddResult.FAILURE);
            return;
        }
        if (addVo.getBoardId() == null) {
            addVo.setResult(ArticleAddResult.ILLEGAL);
            return;
        }
        addVo.setResult(ArticleAddResult.SUCCESS); // 성공
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

    public void getArticle(ArticleListVo articleVo) {
        ArticleListVo articleVo1 = this.boardMapper.selectArticleView(articleVo.getIndex());

        if (articleVo1 == null || articleVo1.getIndex() == 0) {
            articleVo.setResult(ArticleListResult.FAILURE);
            return;
        }

        articleVo.setName(articleVo1.getName());
        articleVo.setBoardId(articleVo1.getBoardId());
        articleVo.setUserEmail(articleVo1.getUserEmail());
        articleVo.setUserNickname(articleVo1.getUserNickname());
        articleVo.setTitle(articleVo1.getTitle());
        articleVo.setContent(articleVo1.getContent());
        articleVo.setLike(articleVo1.getLike());
        articleVo.setCommentCount(articleVo1.getCommentCount());
        articleVo.setView(articleVo1.getView());
        articleVo.setWrittenAt(articleVo1.getWrittenAt());

        articleVo.setResult(ArticleListResult.SUCCESS);

    }

    public List<ArticleListVo> articleList(BoardIdVo boardIdVo) {
        return this.boardMapper.selectArticleList(boardIdVo.getId());
    }

    public List<ArticleListVo> rootArticleList(BoardIdEntity boardIdEntity) {
        return this.boardMapper.selectRootArticleList(boardIdEntity);
    }

    public List<BoardIdEntity> boardList() {
        return this.boardMapper.selectBoardId();
    }

    public void boardIdList(BoardIdVo boardIdVo) {
        BoardIdEntity boardIdEntity = this.boardMapper.selectBoardById(boardIdVo);
        if (boardIdEntity == null || boardIdEntity.getIndex() == 0) {
            boardIdVo.setResult(BoardIdResult.NOT_FOUND);
            return;
        }
        boardIdVo.setName(boardIdEntity.getName());
        boardIdVo.setResult(BoardIdResult.SUCCESS);
    }

    public void modifyArticle(ArticleListVo articleVo) {
        this.boardMapper.updateArticle(articleVo);
        articleVo.setResult(ArticleListResult.SUCCESS);
    }

    public void articleViewCount(ArticleListVo articleVo) {
        this.boardMapper.updateViewCount(articleVo);
    }
    public void articleCommentCount(ArticleListVo articleVo) {
        this.boardMapper.updateCommentCount(articleVo);
    }

    public void addComment(CommentAddVo commentAddVo) {
        if (this.boardMapper.insertComment(commentAddVo) == 0) {
            commentAddVo.setResult(CommentAddResult.FAILURE);
            return;
        }
        commentAddVo.setResult(CommentAddResult.SUCCESS);
    }

    public void commentList(CommentListVo commentListVo) {
        List<CommentListDTO> comments = this.boardMapper.selectComment(commentListVo.getArticleIndex());
        commentListVo.setComments(comments);

        if (comments == null) {
            commentListVo.setResult(CommentListResult.FAILURE);
            return;
        }
        commentListVo.setResult(CommentListResult.SUCCESS);
    }
}
