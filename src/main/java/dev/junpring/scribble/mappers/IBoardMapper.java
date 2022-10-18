package dev.junpring.scribble.mappers;

import dev.junpring.scribble.dtos.ArticleReplyDTO;
import dev.junpring.scribble.dtos.SearchDto;
import dev.junpring.scribble.entities.board.*;
import dev.junpring.scribble.vos.board.article.ArticleListVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface IBoardMapper {
    int deleteArticleList(ArticleEntity articleEntity);
    int insertArticleList(ArticleEntity articleEntity);
    int insertImage(ImageEntity imageEntity);

    ImageEntity selectImage(@Param(value = "id") String id);

    ArticleEntity selectForPrintArticle(@Param(value = "id") int id);

//    List<ArticleListVo> selectArticlesForBoardList(@Param(value = "id") String id);
    int selectArticlesCount(SearchDto params);
    List<ArticleEntity> selectArticlesForBoardList(SearchDto params);

    List<ArticleListVo> selectRootArticleList(BoardEntity _);

    BoardEntity selectBoardByCode(BoardEntity boardIdEntity);

    List<BoardEntity> selectBoardCode();

    int updateArticle(ArticleEntity articleEntity);

    int updateViewCount(@Param(value = "id") int id);


    List<ArticleReplyDTO> getForPrintArticleRepliesFrom(
            @Param(value = "articleId") int articleId);

    int writeArticleReply(ArticleReplyDTO articleReplyDTO);

    ArticleReplyDTO selectArticleReply(@Param(value = "id") int id);

    int updateArticleReply(ArticleReplyDTO articleReplyDTO);

    int deleteArticleReply(ArticleReplyDTO articleReplyDto);

    int insertLikeArticle(@Param(value = "id") int id,
                          @Param(value = "userId") int userId);

    int selectLikePointByUserId(@Param(value = "id") int id,
                                @Param(value = "userId") int userId);

    ArticleEntity selectArticle(@Param(value = "id") int id);

    int selectLikePoint(@Param(value = "id") int id);
    int selectLikePoint2(@Param(value = "id") int id,
                         @Param(value = "userId") int userId);

    int deleteCancelLikeArticle(@Param(value = "id") int id,
                                @Param(value = "userId") int userId);


}
