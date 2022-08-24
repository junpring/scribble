package dev.junpring.scribble.controllers;

import dev.junpring.scribble.entities.board.ArticleEntity;
import dev.junpring.scribble.entities.board.ImageEntity;
import dev.junpring.scribble.entities.member.UserEntity;
import dev.junpring.scribble.enums.board.ArticleAddResult;
import dev.junpring.scribble.enums.board.CommentAddResult;
import dev.junpring.scribble.enums.member.user.LoginResult;
import dev.junpring.scribble.utills.CryptoUtil;
import dev.junpring.scribble.vos.board.article.ArticleAddVo;
import dev.junpring.scribble.vos.board.article.ArticleListVo;
import dev.junpring.scribble.vos.board.BoardIdVo;
import dev.junpring.scribble.services.BoardService;
import dev.junpring.scribble.vos.board.comment.CommentAddVo;
import dev.junpring.scribble.vos.board.comment.CommentListVo;
import dev.junpring.scribble.vos.member.user.UserLoginVo;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller("dev.junpring.scribble.controllers.BoardController")
@RequestMapping(value = "/board")
public class BoardController {
    private final BoardService boardServices;

    @Autowired
    public BoardController(BoardService boardServices) {
        this.boardServices = boardServices;
    }

    @RequestMapping(value = "/{boardId}", method = RequestMethod.GET)
    public ModelAndView getBoard(
            @PathVariable(name = "boardId", required = false) String boardId,
            BoardIdVo boardIdVo,
            ModelAndView modelAndView

    ) {
        boardIdVo.setResult(null);
        boardIdVo.setId(boardId);
        this.boardServices.boardIdList(boardIdVo);
        List<ArticleListVo> articleVos = this.boardServices.articleList(boardIdVo);

        modelAndView.addObject("boardIdVo", boardIdVo);
        modelAndView.addObject("articleVos", articleVos);
        modelAndView.setViewName("board/article");

        System.out.println("--------------result + id + name---------------");
        System.out.println("get result : " + boardIdVo.getResult());
        System.out.println("get boardId : " + boardIdVo.getId());
        System.out.println("get boardName : " + boardIdVo.getName());
        return modelAndView;
    }

    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public ModelAndView PostDelete(
            @RequestParam(value = "pid", required = true) int articleIndex,
            @RequestAttribute(value = "userEntity", required = false) UserEntity userEntity,
            HttpServletResponse response,
            ArticleEntity articleEntity,
            ModelAndView modelAndView
    ) {
        if (userEntity == null) {
            response.setStatus(404);
            return null;
        }
        articleEntity.setIndex(articleIndex);
        this.boardServices.removeArticle(articleEntity);

        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }

    @RequestMapping(value = "modify", method = RequestMethod.GET)
    public ModelAndView getModify(
            @RequestAttribute(value = "userEntity", required = false) UserEntity userEntity,
            @RequestParam(value = "pid", required = true) int articleIndex,
            UserLoginVo loginVo,
            ModelAndView modelAndView
    ) {

        ArticleListVo articleVo = new ArticleListVo();
        articleVo.setIndex(articleIndex);
        articleVo.setUserEmail(userEntity.getEmail());
        this.boardServices.getArticle(articleVo);

        if (userEntity == null || userEntity.getLevel() == 10 || !userEntity.getEmail().equals(articleVo.getUserEmail())) {
            loginVo.setResult(LoginResult.NOT_FOUND);
        } else {
            loginVo.setResult(LoginResult.SUCCESS);
        }


        modelAndView.addObject("loginVo", loginVo);
        modelAndView.addObject("articleVo", articleVo);
        modelAndView.setViewName("board/modify");
        return modelAndView;
    }

    @RequestMapping(value = "modify", method = RequestMethod.POST)
    public ModelAndView postModify(
            @RequestAttribute(value = "userEntity", required = false) UserEntity userEntity,
            @RequestParam(value = "pid", required = true) int articleIndex,
            UserLoginVo loginVo,
            ArticleListVo articleListVo,
            ModelAndView modelAndView
    ) {
        articleListVo.setIndex(articleIndex);
        articleListVo.setUserEmail(userEntity.getEmail());

        if (userEntity == null || userEntity.getLevel() == 10) {
            loginVo.setResult(LoginResult.NOT_FOUND);
        } else {
            loginVo.setResult(LoginResult.SUCCESS);
        }
        this.boardServices.modifyArticle(articleListVo);
        System.out.println("modify result: " + articleListVo.getResult());
        System.out.println("title: " + articleListVo.getTitle());

        modelAndView.addObject("articleVo", articleListVo);
        modelAndView.setViewName("redirect:/board/view?index=" + articleIndex);
        return modelAndView;
    }

    @RequestMapping(value = "/{boardId}/add", method = RequestMethod.GET)
    public ModelAndView getIdAdd(
            @PathVariable(name = "boardId", required = true) String boardId,
            BoardIdVo boardIdVo,
            ModelAndView modelAndView
    ) {
        boardIdVo.setId(boardId);
        this.boardServices.boardIdList(boardIdVo);

        modelAndView.addObject("boardIdVo", boardIdVo);
        modelAndView.setViewName("board/boardIdAdd");
        return modelAndView;
    }

    @RequestMapping(value = "/{boardId}/add", method = RequestMethod.POST)
    public ModelAndView postIdAdd(
            @RequestAttribute(value = "userEntity", required = false) UserEntity userEntity,
            ArticleAddVo addVo,
            ModelAndView modelAndView
    ) {
        addVo.setUserEmail(userEntity.getEmail());
        addVo.setUserNickname(userEntity.getNickname());

        this.boardServices.addArticle(addVo);
        if (addVo.getResult() == ArticleAddResult.SUCCESS) {
            modelAndView.setViewName("redirect:/board/view?index=" + addVo.getIndex());
        }
        return modelAndView;
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView getAdd(
            ModelAndView modelAndView
    ) {
        modelAndView.setViewName("board/add");
        return modelAndView;
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ModelAndView postAdd(
            @RequestAttribute(value = "userEntity", required = false) UserEntity userEntity,
            ArticleAddVo addVo,
            ModelAndView modelAndView
    ) {
        addVo.setResult(null);
        addVo.setUserEmail(userEntity.getEmail());
        addVo.setUserNickname(userEntity.getNickname());

        this.boardServices.addArticle(addVo);
        if (addVo.getResult() == ArticleAddResult.SUCCESS) {
            modelAndView.setViewName("redirect:/board/view?index=" + addVo.getIndex());

        } else {
            modelAndView.addObject("addVo", addVo);
            modelAndView.setViewName("/board/add");
        }
        return modelAndView;
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

    @RequestMapping(value = "view", method = RequestMethod.GET)
    public ModelAndView getView(
            @RequestParam(name = "index", required = false) int index,
            ArticleListVo articleVo,
            CommentListVo commentListVo,
            HttpServletRequest request,
            HttpServletResponse response,
            ModelAndView modelAndView
    ) {
        articleVo.setIndex(index);
        this.boardServices.getArticle(articleVo);
        this.boardServices.articleCommentCount(articleVo);
//      쿠키 value 해싱 해야됨.
        Cookie[] cookies = request.getCookies();

        if (cookies != null) { // 쿠키가 있으면
            for (Cookie cookie : cookies) {
//                System.out.println("----------------------------");
//                System.out.println("cookie.getName " + cookie.getName());
//                System.out.println("cookie.getValue " + cookie.getValue());
                if (!cookie.getValue().contains(request.getParameter("index")) && cookie.getName().equals("visit_cookie")) {
                    cookie.setValue(cookie.getValue() + "_" + request.getParameter("index"));
                    cookie.setMaxAge(60 * 60 * 2);  /* 쿠키 시간 */
                    response.addCookie(cookie);
                    this.boardServices.articleViewCount(articleVo);
                }
            }
        } else {
            Cookie newCookie = new Cookie("visit_cookie", request.getParameter("index"));
            newCookie.setMaxAge(60 * 60 * 2);
            response.addCookie(newCookie);
            this.boardServices.articleViewCount(articleVo);
        }

        commentListVo.setArticleIndex(articleVo.getIndex());
        commentListVo.setResult(null);
        this.boardServices.commentList(commentListVo);
        modelAndView.addObject("commentListVo", commentListVo);
        modelAndView.addObject("articleVo", articleVo);
        modelAndView.setViewName("board/view");
        return modelAndView;
    }

    @RequestMapping(value = "comment-add", method = RequestMethod.POST)
    @ResponseBody
    public String postComment(
            @RequestAttribute(name = "userEntity", required = false) UserEntity userEntity,
            CommentAddVo commentAddVo
    ) {
        commentAddVo.setResult(null);
        if (userEntity == null) {
            commentAddVo.setResult(CommentAddResult.USER_NOT_FOUND);
            JSONObject responseJson = new JSONObject();
            responseJson.put("comment-result", commentAddVo.getResult().name());
            return responseJson.toString();
        }
        commentAddVo.setUserIndex(userEntity.getIndex());
        commentAddVo.setUserEmail(userEntity.getEmail());
        this.boardServices.addComment(commentAddVo);

        System.out.println(commentAddVo.getResult());

        JSONObject responseJson = new JSONObject();
        responseJson.put("comment-result", commentAddVo.getResult().name());
        return responseJson.toString();
    }
}
