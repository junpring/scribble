package dev.junpring.scribble.mappers;

import dev.junpring.scribble.dtos.CommentListDTO;
import dev.junpring.scribble.entities.board.ArticleEntity;
import dev.junpring.scribble.entities.board.BoardIdEntity;
import dev.junpring.scribble.entities.board.CommentEntity;
import dev.junpring.scribble.entities.board.ImageEntity;
import dev.junpring.scribble.vos.board.article.ArticleListVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IBoardMapper {
    int deleteArticleList(ArticleEntity articleEntity);

    int insertComment(CommentEntity commentEntity);

    int insertArticleList(ArticleEntity articleEntity);

    int insertImage(ImageEntity imageEntity);


    List<CommentListDTO> selectComment(@Param(value = "articleIndex") int articleIndex);

    ImageEntity selectImage(@Param(value = "id") String id);

    ArticleListVo selectArticleView(@Param(value = "index") int index);

    List<ArticleListVo> selectArticleList(@Param(value = "id") String id);

    List<ArticleListVo> selectRootArticleList(BoardIdEntity _);

    BoardIdEntity selectBoardById(BoardIdEntity boardIdEntity);

    List<BoardIdEntity> selectBoardId();

    int updateArticle(ArticleEntity articleEntity);

    int updateViewCount(ArticleEntity articleEntity);

    int updateCommentCount(ArticleEntity articleEntity);
}
