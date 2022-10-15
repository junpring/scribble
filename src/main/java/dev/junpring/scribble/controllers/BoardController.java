package dev.junpring.scribble.controllers;

import dev.junpring.scribble.dtos.ArticleListDto;
import dev.junpring.scribble.dtos.ArticleReplyDTO;
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
        PagingResponse<ArticleEntity> articlesResponse = this.boardServices.getArticlesForBoardList(params);
        model.addAttribute("boardIdVo", boardIdVo);
        model.addAttribute("articlesResponse", articlesResponse);
        return "board/list";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String getSearch(
    ) {

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
        return modelAndView;
    }

    @RequestMapping(value = "write", method = RequestMethod.GET)
    public ModelAndView getWrite(
            ModelAndView modelAndView
    ) {
        modelAndView.setViewName("board/write");
        return modelAndView;
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
            articleWriteVo.setMsg(String.format("%d번 게시글이 생성되었습니다.", articleWriteVo.getId()));
            model.addAttribute("alertMsg", articleWriteVo.getMsg());
            model.addAttribute("locationReplace", "./detail?id=" + articleWriteVo.getId());
            model.addAttribute("articleWriteVo", articleWriteVo);
            return "common/redirect";
        }
        return "common/redirect";
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
        HttpStatus status = HttpStatus.NOT_FOUND; // 404 담음
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
        return responseJson.toString(); // Json 리턴, 업로드 성공했다는 응답을 돌려주기위함.
    }

    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public String getDetail(
            @RequestParam(value = "id", required = false) int id,
            ArticleListDto articleListDto,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response

    ) {
        articleListDto.setId(id);
        this.boardServices.articleViewCount(articleListDto.getId());
        this.boardServices.getArticle(articleListDto);
        System.out.println(articleListDto.getView());

//      쿠키 value 해싱 해야됨.
        Cookie[] cookies = request.getCookies();
//
//        if (cookies != null) { // 쿠키가 있으면
//            for (Cookie cookie : cookies) {
////                System.out.println("----------------------------");
////                System.out.println("cookie.getName " + cookie.getName());
////                System.out.println("cookie.getValue " + cookie.getValue());
//                if (!cookie.getValue().contains(request.getParameter("id")) && cookie.getName().equals("visit_cookie")) {
//                    cookie.setValue(cookie.getValue() + "_" + request.getParameter("id"));
//                    cookie.setMaxAge(60 * 60 * 2);  /* 쿠키 시간 */
//                    response.addCookie(cookie);
//                    this.boardServices.articleViewCount(articleListDto);
//                }
//            }
//        } else {
//            Cookie newCookie = new Cookie("visit_cookie", request.getParameter("id"));
//            newCookie.setMaxAge(60 * 60 * 2);
//            response.addCookie(newCookie);
//            this.boardServices.articleViewCount(articleListDto);
//        }

        List<ArticleReplyDTO> articleReplyDtos = boardServices.getForPrintArticleReplies(articleListDto.getId());
        model.addAttribute("articleReplyDtos", articleReplyDtos);
        model.addAttribute("articleListDto", articleListDto);
        return "board/detail";
    }

    @RequestMapping(value = "getForPrintArticleRepliesRs", method = RequestMethod.GET)
    @ResponseBody
    public List<ArticleReplyDTO> getForPrintArticleRepliesRs(
            @RequestParam(value = "id", required = true) int id,
            ArticleReplyDTO articleReplyDTO
    ) {
        List<ArticleReplyDTO> articleReplies = this.boardServices.getForPrintArticleReplies(id);

        articleReplyDTO.setMsg(String.format("총 %d개의 댓글이 있습니다.", articleReplies.size()));
        articleReplyDTO.setResultCode("S-1");

        return articleReplies;
    }

    @RequestMapping(value = "postWriteReply", method = RequestMethod.POST)
    @ResponseBody
    public ArticleReplyDTO postWriteReply(
            @RequestAttribute(value = "userEntity", required = false) UserEntity userEntity,
            ArticleReplyDTO articleReplyDTO
    ) {
        int userId = userEntity.getId();
        articleReplyDTO.setUserId(userId);
        this.boardServices.writeReply(articleReplyDTO);
        return articleReplyDTO;
    }

    @RequestMapping(value = "postModifyReply", method = RequestMethod.POST)
    @ResponseBody
    public ArticleReplyDTO postModifyReply(
            @RequestParam(value = "id") int id,
            @RequestAttribute(value = "userEntity", required = false) UserEntity userEntity,
            ArticleReplyDTO articleReplyDto
    ) {
        int userId = userEntity.getId();

        ArticleReplyDTO articleModifyReplyAvailableRs = this.boardServices.getArticleModifyReplyAvailable(id, userId);

        if ((articleModifyReplyAvailableRs.getResultCode()).startsWith("F-")) {
            return articleModifyReplyAvailableRs;
        }
        this.boardServices.modifyArticleReply(articleReplyDto);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return articleReplyDto;
    }

    @RequestMapping(value = "postDeleteReply", method = RequestMethod.POST)
    @ResponseBody
    public ArticleReplyDTO postDeleteReply(
            int id,
            @RequestAttribute(value = "userEntity", required = false) UserEntity userEntity,
            ArticleReplyDTO articleReplyDto
    ) {
        int userId = userEntity.getId();

        ArticleReplyDTO articleDeleteReplyAvailableRs = this.boardServices.getArticleDeleteReplyAvailableRs(id, userId);

        if ((articleDeleteReplyAvailableRs.getResultCode()).startsWith("F-")) {
            return articleDeleteReplyAvailableRs;
        }
        this.boardServices.deleteArticleReply(articleReplyDto);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return articleReplyDto;
    }

}
