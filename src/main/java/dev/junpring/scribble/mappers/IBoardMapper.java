package dev.junpring.scribble.mappers;

import dev.junpring.scribble.dtos.ArticleCommentDto;
import dev.junpring.scribble.dtos.ArticleListDto;
import dev.junpring.scribble.dtos.ArticleSearchDto;
import dev.junpring.scribble.entities.board.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IBoardMapper {
    int deleteArticleList(ArticleEntity articleEntity);

    int insertArticleList(ArticleEntity articleEntity);

    int insertImage(ImageEntity imageEntity);

    ImageEntity selectImage(@Param(value = "id") String id);

    ArticleEntity selectForPrintArticle(@Param(value = "id") int id);

    int selectArticlesBoardListCount(ArticleSearchDto params);

    int selectArticlesCount(ArticleSearchDto params);
    int selectUserArticlesCount(ArticleSearchDto params);

    List<ArticleListDto> selectArticlesForBoardList(ArticleSearchDto params);

    List<ArticleListDto> selectUserArticlesForList(ArticleSearchDto params);

    List<ArticleListDto> selectFindArticlesForList(ArticleSearchDto params);

    List<ArticleEntity> selectHomeArticleList(BoardEntity _);

    BoardEntity selectBoardByCode(BoardEntity boardIdEntity);

    List<BoardEntity> selectBoardList();

    int updateArticle(ArticleEntity articleEntity);

    int updateViewCount(@Param(value = "id") int id);

    int updateCommentCount(@Param(value = "id") int id);

    int updateLikeCount(int id);

    List<ArticleCommentDto> selectForPrintArticleRepliesFrom(
            @Param(value = "articleId") int articleId);

    int writeArticleReply(ArticleCommentDto articleReplyDTO);

    ArticleCommentDto selectArticleReply(@Param(value = "id") int id);

    int updateArticleReply(ArticleCommentDto articleReplyDTO);

    int deleteArticleReply(ArticleCommentDto articleReplyDto);

    int insertLikeArticle(@Param(value = "id") int id,
                          @Param(value = "userId") int userId);

    int selectLikePointByUserId(@Param(value = "id") int id,
                                @Param(value = "userId") int userId);

    ArticleEntity selectArticle(@Param(value = "id") int id);

    int selectLikePoint(@Param(value = "id") int id);

    int deleteCancelLikeArticle(@Param(value = "id") int id,
                                @Param(value = "userId") int userId);


    int updateDeletedReply(
            @Param(value = "isDeleted") boolean isDeleted,
            @Param(value = "userId") int userId);

    List<ArticleListDto> selectRcmdArticles();


}
