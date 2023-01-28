package dev.junpring.scribble.controllers;

import dev.junpring.scribble.dtos.ArticleCommentDto;
import dev.junpring.scribble.dtos.ArticleLikeDto;
import dev.junpring.scribble.dtos.ArticleListDto;
import dev.junpring.scribble.dtos.SearchDto;
import dev.junpring.scribble.entities.board.*;
import dev.junpring.scribble.entities.member.UserEntity;
import dev.junpring.scribble.enums.board.ArticleWriteResult;
import dev.junpring.scribble.utills.CryptoUtil;
import dev.junpring.scribble.vos.board.article.ArticleWriteVo;
import dev.junpring.scribble.vos.board.article.ArticleListVo;
import dev.junpring.scribble.vos.board.BoardIdVo;
import dev.junpring.scribble.services.BoardService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller("dev.junpring.scribble.controllers.BoardController")
@RequestMapping(value = "/board")
public class BoardController {
    private final BoardService boardServices;

    @Autowired
    public BoardController(BoardService boardServices) {
        this.boardServices = boardServices;
    }

    @RequestMapping(value = "/list/best", method = RequestMethod.GET)
    public String getBestBoard(
            Model model
    ) {
        List<ArticleListDto> getForPrintRcmdArticlesRs = this.boardServices.getForPrintRcmdArticles();
        model.addAttribute("getForPrintRcmdArticlesRs", getForPrintRcmdArticlesRs);
        return "board/best-list";
    }

    @RequestMapping(value = "/list/{boardCode}", method = RequestMethod.GET)
    public String getBoard(
            @PathVariable(name = "boardCode", required = false) String boardCode,
            @ModelAttribute("params") final SearchDto params,
            BoardIdVo boardIdVo,
            Model model
    ) {
        boardIdVo.setResult(null);
        boardIdVo.setCode(boardCode);
        this.boardServices.boardIdList(boardIdVo);
        PagingResponse<ArticleListDto> articlesResponse = this.boardServices.getArticlesForBoardList(params);
        model.addAttribute("boardIdVo", boardIdVo);
        model.addAttribute("articlesResponse", articlesResponse);
        return "board/list";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String getSearch(
            @ModelAttribute("params") final SearchDto params,
            Model model
    ) {
        PagingResponse<ArticleListDto> articlesResponse = this.boardServices.getFindArticlesForList(params);
        List<BoardEntity> boardEntities = this.boardServices.getBoardList();
        model.addAttribute("boardEntities", boardEntities);
        model.addAttribute("articlesResponse", articlesResponse);
        return "board/search";
    }

    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public String getDelete(
            @RequestParam(value = "id", required = true) int id,
            @RequestAttribute(value = "userEntity", required = false) UserEntity userEntity,
            ArticleListVo articleListVo,
            Model model
    ) {
        int userId = userEntity.getId();
        articleListVo.setId(id);
        this.boardServices.getForPrintArticle(articleListVo, userId);
        if ((articleListVo.getResultCode()).startsWith("F-")) {
            model.addAttribute("alertMsg", articleListVo.getMsg());
            model.addAttribute("historyBack", true);
            return "common/redirect";
        }
        this.boardServices.removeArticle(articleListVo);
        model.addAttribute("locationReplace", "/");
        return "common/redirect";
    }

    @RequestMapping(value = "modify", method = RequestMethod.GET)
    public String getModify(
            @RequestParam(value = "id", required = true) int id,
            @RequestAttribute(value = "userEntity", required = false) UserEntity userEntity,
            ArticleListVo articleListVo,
            Model model
    ) {
        int userId = userEntity.getId();
        articleListVo.setId(id);
        articleListVo.setUserId(userId);
        this.boardServices.getForPrintArticle(articleListVo, userId);

        if ((articleListVo.getResultCode()).startsWith("F-")) {
            model.addAttribute("alertMsg", articleListVo.getMsg());
            model.addAttribute("historyBack", true);
            return "common/redirect";
        }
        model.addAttribute("articleListVo", articleListVo);
        return "board/modify";
    }

    @RequestMapping(value = "modify", method = RequestMethod.POST)
    public ModelAndView postModify(
            @RequestAttribute(value = "userEntity", required = false) UserEntity userEntity,
            @RequestParam(value = "id", required = true) int id,
            ArticleListVo articleListVo,
            ModelAndView modelAndView
    ) {
        articleListVo.setId(id);
        articleListVo.setUserId(userEntity.getId());
        this.boardServices.modifyArticle(articleListVo);
        modelAndView.addObject("articleVo", articleListVo);
        modelAndView.setViewName("redirect:/board/detail?id=" + id);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return modelAndView;
    }

    @RequestMapping(value = "write", method = RequestMethod.GET)
    public String getWrite(
            Model model
    ) {

        HashMap<String, String> boardCodes = new HashMap<>();
        List<BoardEntity> boardEntities = this.boardServices.getBoardList();
        for (BoardEntity boardEntity : boardEntities) {
            boardCodes.put(boardEntity.getCode(), boardEntity.getName());
        }

        model.addAttribute("boardCodes", boardCodes);
        return "board/write";
    }

    @RequestMapping(value = "write", method = RequestMethod.POST)
    public String postWrite(
            @RequestAttribute(value = "userEntity", required = false) UserEntity userEntity,
            ArticleWriteVo articleWriteVo,
            Model model
    ) {
        articleWriteVo.setResult(null);
        articleWriteVo.setUserId(userEntity.getId());

        this.boardServices.writeArticle(articleWriteVo);
        if (articleWriteVo.getResult() == ArticleWriteResult.SUCCESS) {
            model.addAttribute("articleWriteVo", articleWriteVo);
        }
        return "redirect:/board/detail?id=" + articleWriteVo.getId();
    }

    @RequestMapping(value = "download-image", method = RequestMethod.GET)
    @ResponseBody
    // 서버에서 클라이언트로 응답 데이터를 전송하기 위해 자바 객체를 HTTP 응답 본문의 객체로 변환하여 클라이언트로 전송
    public ResponseEntity<byte[]> getDownloadImage(
            @RequestParam(name = "id", required = false) String id
    ) {
        ImageEntity imageEntity = null;
        if (id != null) {
            imageEntity = this.boardServices.getImage(id);
        }
        byte[] data = null;
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.NOT_FOUND;
        if (imageEntity != null && imageEntity.getData() != null) {
            data = imageEntity.getData();
            headers.add("Content-Type", imageEntity.getFileType());
            headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", imageEntity.getFileName()));
            headers.add("Content-Length", String.valueOf(data.length));
            status = HttpStatus.OK;
        }
        return new ResponseEntity<>(data, headers, status);
    }

    @RequestMapping(value = "add/upload-image", method = RequestMethod.POST)
    @ResponseBody
    public String postAddUploadImage(
            @RequestParam(name = "upload") MultipartFile[] images
    ) throws IOException {
        ImageEntity[] imageEntities = new ImageEntity[images.length];
        for (int i = 0; i < images.length; i++) {
            MultipartFile image = images[i];
            Date createAt = new Date(); // 현재시간 생성
            String id = String.format("%s%s%f",
                    new SimpleDateFormat("yyyyMMddHHmmssSSS").format(createAt),
                    image.getOriginalFilename(), // 이미지의 파일이름과 확장자
                    Math.random());
            id = CryptoUtil.hashSha512(id); // 해싱
            imageEntities[i] = new ImageEntity(id, createAt,
                    image.getOriginalFilename(),
                    image.getContentType(),
                    image.getBytes());
        }
        this.boardServices.uploadImages(imageEntities);
        JSONObject responseJson = new JSONObject();
        JSONArray urlJson = new JSONArray();
        for (ImageEntity imageEntity : imageEntities) {
            urlJson.put(String.format("http://127.0.0.1:8080/board/download-image?id=%s", imageEntity.getId()));
        }
        responseJson.put("url", urlJson);
        return responseJson.toString();
    }

    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public String getDetail(
            @RequestParam(value = "id") int id,
            ArticleListDto articleListDto,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response

    ) {
        int userId = (int) request.getAttribute("connectedUserId");
        articleListDto.setId(id);
        this.boardServices.getArticle(articleListDto);
        this.boardServices.articleCommentCount(articleListDto.getId());
        this.boardServices.articleLikeCount(articleListDto.getId());

        // 쿠키 value 해싱 해야됨.
        Cookie[] cookies = request.getCookies();
        Cookie viewCookie = null;

        // 쿠키가 있을 경우
        if (cookies != null && cookies.length > 0) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals("visited" + id)) {
                    viewCookie = cookies[i];
                }
            }
        }
        if (viewCookie == null) {
            System.out.println("----------------");
            System.out.println("cookie 없음");
            Cookie newCookie = new Cookie("visited" + id, "|" + id + "|");
            response.addCookie(newCookie);
            // 쿠키를 추가 시키고 조회수 증가시킴
            this.boardServices.articleViewCount(articleListDto.getId());
        }
        else {
            System.out.println("----------------");
            System.out.println("cookie 있음");
            String value = viewCookie.getValue();
            System.out.println("cookie 값 : " + value);
        }

        List<ArticleCommentDto> articleCommentDtos = this.boardServices.getForPrintArticleReplies(articleListDto.getId());
        int likePointByUserId = this.boardServices.getLikePointByUserId(id, userId);

        model.addAttribute("articleCommentDtos", articleCommentDtos);
        model.addAttribute("articleListDto", articleListDto);
        model.addAttribute("likePointByUserId", likePointByUserId);

        List<ArticleListDto> getForPrintRcmdArticlesRs = this.boardServices.getForPrintRcmdArticles();
        model.addAttribute("getForPrintRcmdArticlesRs", getForPrintRcmdArticlesRs);


        if ((articleListDto.getResultCode()).startsWith("F-")) {
            model.addAttribute("alertMsg", articleListDto.getMsg());
            model.addAttribute("historyBack", true);
            return "common/redirect";
        }
        return "board/detail";
    }

    @RequestMapping(value = "getForPrintArticleCommentsRs", method = RequestMethod.GET)
    @ResponseBody
    public List<ArticleCommentDto> getForPrintArticleRepliesRs(
            @RequestParam(value = "id", required = true) int id
    ) {
        List<ArticleCommentDto> commentDtoList = this.boardServices.getForPrintArticleReplies(id);
        /*
        articleReplyDTO.setMsg(String.format("총 %d개의 댓글이 있습니다.", articleReplies.size()));
        articleReplyDTO.setResultCode("S-1");
        */
        return commentDtoList;
    }


    @RequestMapping(value = "postWriteReply", method = RequestMethod.POST)
    @ResponseBody
    public ArticleCommentDto postWriteReply(
            @RequestAttribute(value = "connectedUserId", required = false) int userId,
            ArticleCommentDto articleReplyDTO
    ) {
        articleReplyDTO.setUserId(userId);
        this.boardServices.writeReply(articleReplyDTO);
        return articleReplyDTO;
    }

    @RequestMapping(value = "postModifyReply", method = RequestMethod.POST)
    @ResponseBody
    public ArticleCommentDto postModifyReply(
            @RequestParam(value = "id") int id,
            @RequestAttribute(value = "userEntity", required = false) UserEntity userEntity,
            ArticleCommentDto articleCommentDto
    ) {
        int userId = userEntity.getId();

        ArticleCommentDto articleModifyReplyAvailableRs = this.boardServices.getArticleModifyReplyAvailable(id, userId);

        if ((articleModifyReplyAvailableRs.getResultCode()).startsWith("F-")) {
            return articleModifyReplyAvailableRs;
        }
        this.boardServices.modifyArticleReply(articleCommentDto);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return articleCommentDto;
    }

    @RequestMapping(value = "postDeleteReply", method = RequestMethod.POST)
    @ResponseBody
    public ArticleCommentDto postDeleteReply(
            int id,
            @RequestAttribute(value = "userEntity", required = false) UserEntity userEntity,
            ArticleCommentDto articleCommentDto
    ) {
        int userId = userEntity.getId();

        ArticleCommentDto articleDeleteReplyAvailableRs = this.boardServices.getArticleDeleteReplyAvailableRs(id, userId);

        if ((articleDeleteReplyAvailableRs.getResultCode()).startsWith("F-")) {
            return articleDeleteReplyAvailableRs;
        }
        this.boardServices.deleteArticleReply(articleCommentDto);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return articleCommentDto;
    }

    @RequestMapping(value = "postInsertLike", method = RequestMethod.POST)
    @ResponseBody
    public ArticleLikeDto postInsertLike(
            @RequestParam(value = "id") int id,
            @RequestAttribute(value = "connectedUserId", required = false) int userId
    ) {
        ArticleLikeDto articleLikeDto = new ArticleLikeDto();
        ArticleLikeDto articleLikeAvailableRs = this.boardServices.getArticleLikeAvailable(id, userId);

        if ((articleLikeAvailableRs.getResultCode().startsWith("F-"))) {
            articleLikeDto.setResultCode(articleLikeAvailableRs.getResultCode());
            articleLikeDto.setMsg(articleLikeAvailableRs.getMsg());

            return articleLikeDto;
        }

        articleLikeDto = this.boardServices.addLikeArticle(id, userId);
        int likePoint = this.boardServices.getLikePoint(id);
        int likePointByUserId = this.boardServices.getLikePointByUserId(id, userId);
        articleLikeDto.setLikePoint(likePoint);
        articleLikeDto.setLikePointByUserId(likePointByUserId);
        return articleLikeDto;
    }

    @RequestMapping(value = "postDeleteLike", method = RequestMethod.POST)
    @ResponseBody
    public ArticleLikeDto postDeleteLike(
            @RequestParam(value = "id") int id,
            @RequestAttribute(value = "connectedUserId", required = false) int userId
    ) {
        ArticleLikeDto articleLikeDto = new ArticleLikeDto();

        ArticleLikeDto articleCancelLikeAvailableRs = this.boardServices.getArticleCancelLikeAvailable(id, userId);

        if ((articleCancelLikeAvailableRs.getResultCode().startsWith("F-"))) {
            articleLikeDto.setResultCode(articleCancelLikeAvailableRs.getResultCode());
            articleLikeDto.setMsg(articleCancelLikeAvailableRs.getMsg());

            return articleLikeDto;
        }

        articleLikeDto = this.boardServices.cancelLikeArticle(id, userId);
        int likePoint = this.boardServices.getLikePoint(id);
        int likePointByUserId = this.boardServices.getLikePointByUserId(id, userId);
        articleLikeDto.setLikePoint(likePoint);
        articleLikeDto.setLikePointByUserId(likePointByUserId);
        return articleLikeDto;
    }
}
