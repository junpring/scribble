package dev.junpring.scribble.controllers;

import dev.junpring.scribble.dtos.ArticleListDto;
import dev.junpring.scribble.dtos.ArticleSearchDto;
import dev.junpring.scribble.entities.board.PagingResponse;
import dev.junpring.scribble.entities.member.UserEntity;
import dev.junpring.scribble.enums.member.user.LoginResult;
import dev.junpring.scribble.entities.member.SessionEntity;
import dev.junpring.scribble.services.BoardService;
import dev.junpring.scribble.services.UserService;
import dev.junpring.scribble.vos.member.user.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller(value = "dev.junpring.scribble.controllers.UserController")
@RequestMapping(value = "/user")
public class UserController {
    private final UserService userService;
    private final BoardService boardService;
    private static final int DEFAULT_LEVEL = 9; // 기본값 레벨(9) : 일반 유저

    @Autowired
    public UserController(UserService userService, BoardService boardService) {
        this.userService = userService;
        this.boardService = boardService;
    }

    @RequestMapping(value = "check-email", method = RequestMethod.POST)
    @ResponseBody
    public String postEmailCheck(
            UserEntity userEntity
    ) {
        int emailCount = this.userService.CountByEmail(userEntity);
        return String.valueOf(emailCount);
    }

    @RequestMapping(value = "check-nickname", method = RequestMethod.POST)
    @ResponseBody
    public String postNicknameCheck(
            UserEntity userEntity
    ) {
        int nicknameCount = this.userService.CountByNickname(userEntity);
        return String.valueOf(nicknameCount);
    }

    @RequestMapping(value = "register", method = RequestMethod.GET)
    public ModelAndView getRegister(
            ModelAndView modelAndView
    ) {
        modelAndView.setViewName("user/register");
        return modelAndView;
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ResponseBody
    public String postRegister(
            UserRegisterVo registerVo
    ) throws MessagingException {
        registerVo.setLevel(DEFAULT_LEVEL);
        registerVo.setId(0);
        registerVo.setDeleted(false);
        registerVo.setSuspended(false);
        registerVo.setEmailVerified(false);
        registerVo.setResult(null);
        this.userService.register(registerVo);
        JSONObject obj = new JSONObject();
        obj.put("result", registerVo.getResult().name());
        return obj.toString();
    }

    @RequestMapping(value = "find-email", method = RequestMethod.GET)
    public String getRecover(
    ) {
        return "user/find-email";
    }

//    @RequestMapping(value = "recover", method = RequestMethod.POST)
//    @ResponseBody
//    public String postRecover(
//            UserFindVo userFindVo
//    ) throws  MessagingException{
//        userFindVo.setResult(null);
//        this.userService.passwordRecover(userFindVo);
//        JSONObject obj = new JSONObject();
//        obj.put("result", userFindVo.getResult().name());
//        System.out.println("getResult: " + userFindVo.getResult().name());
//        return obj.toString();
//    }

    @RequestMapping(value = "find-email", method = RequestMethod.POST)
    @ResponseBody
    public String postFindUserEmail(
                                     UserFindVo userFindVo
    ) {
        userFindVo.setResult(null);
        this.userService.findEmail(userFindVo);

        JSONObject obj = new JSONObject();
        obj.put("result", userFindVo.getResult().name());
        obj.put("userEmail", userFindVo.getEmail());
        obj.put("userNickname", userFindVo.getNickname());
        return obj.toString();
    }

    @RequestMapping(value = "verify-email", method = RequestMethod.GET)
    public ModelAndView getVerifyEmail(
            @RequestParam(value = "c", required = true) String code,
            @RequestParam(value = "s", required = true) String salt,
            UserEmailVerifyCodeVo userEmailVerifyCodeVo,
            ModelAndView modelAndView
    ) {
        userEmailVerifyCodeVo.setId(0);
        userEmailVerifyCodeVo.setCode(code);
        userEmailVerifyCodeVo.setSalt(salt);
        userEmailVerifyCodeVo.setResult(null);
        this.userService.verifyEmail(userEmailVerifyCodeVo);
        modelAndView.addObject("userEmailVerifyCodeVo", userEmailVerifyCodeVo);
        modelAndView.setViewName("user/verifyEmail");

        return modelAndView;
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public ModelAndView getLogin(
            ModelAndView modelAndView
    ) {
        modelAndView.setViewName("user/login");
        return modelAndView;
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public String postLogin(
            UserLoginVo loginVo,
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "remember-me", required = false) boolean remember
            // checkbox ox
    ) {
        loginVo.setResult(null);
        loginVo.setAutologin(remember);
        this.userService.login(loginVo, request);

        if (loginVo.getResult() == LoginResult.SUCCESS) {
            Cookie sessionKeyCookie = new Cookie("sk", loginVo.getSessionEntity().getKey());
            if (loginVo.isAutologin()) {
                sessionKeyCookie.setMaxAge(60 * 60 * 24 * 7);
            }
            sessionKeyCookie.setPath("/");
            // 위 설정하지않으면 기본 path : 현재주소 (/user/**) 이 주소에만 사용가능, / 는 전역 사용가능.
            response.addCookie(sessionKeyCookie); // response에 생성한 쿠키를 추가. (응답보낼 쿠키)
        }
        JSONObject obj = new JSONObject();
        obj.put("result", loginVo.getResult().name());
        return obj.toString();
    }

    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public ModelAndView getLogout(
            ModelAndView modelAndView,
            HttpServletRequest request
    ) {
        SessionEntity sessionEntity = null;
        if (request.getAttribute("sessionEntity") instanceof SessionEntity) {
            sessionEntity = (SessionEntity) request.getAttribute("sessionEntity");
            this.userService.expireSession(sessionEntity);
        }
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }

    @RequestMapping(value = "my", method = RequestMethod.GET)
    public String getMy(
            @RequestAttribute(value = "userEntity", required = false)
            UserEntity userEntity
    ) {
        this.userService.getUserId(userEntity.getId());
        System.out.println(userEntity.getId());
        System.out.println(userEntity.getNickname());
        return "user/my";
    }

    @RequestMapping(value = "modify-password", method = RequestMethod.GET)
    public String getModifyPassword(

    ) {
        return "user/modify-password";
    }

    @RequestMapping(value = "modify-password", method = RequestMethod.POST)
    @ResponseBody
    public String postModifyPassword(
            @RequestAttribute(value = "userEntity", required = false)
            UserEntity userEntity,
            UserModifyPasswordVo userModifyPasswordVo

    ) {
        userModifyPasswordVo.setId(userEntity.getId());
        this.userService.modifyPassword(userModifyPasswordVo);
        JSONObject object = new JSONObject();
        object.put("result", userModifyPasswordVo.getResult().name());
        return object.toString();
    }


    @RequestMapping(value = "modify-nickname", method = RequestMethod.GET)
    public String getModifyNickname(

    ) {
        return "user/modify-nickname";
    }


    @RequestMapping(value = "modify-nickname", method = RequestMethod.POST)
    @ResponseBody
    public UserModifyNicknameVo postModifyNickname(
            @RequestAttribute(value = "userEntity", required = false)
            UserEntity userEntity,
            UserModifyNicknameVo userModifyNicknameVo

    ) {
        userModifyNicknameVo.setId(userEntity.getId());
        userModifyNicknameVo.setModifyExpiresAt(userEntity.getModifyExpiresAt());
        this.userService.modifyNickname(userModifyNicknameVo);
        return userModifyNicknameVo;
    }

    @RequestMapping(value = "withdrawal", method = RequestMethod.GET)
    public String getWithdrawal(
    ) {
        return "user/withdrawal";
    }

    @RequestMapping(value = "withdrawal", method = RequestMethod.POST)
    @ResponseBody
    public UserWithdrawalVo postWithdrawal(
            @RequestAttribute(value = "userEntity", required = false)
            UserEntity userEntity,
            UserWithdrawalVo userWithdrawalVo
    ) {
        userWithdrawalVo.setId(userEntity.getId());
        userWithdrawalVo.setLevel(0);
        userWithdrawalVo.setDeleted(false);
        userWithdrawalVo.setResult(null);
        this.userService.deletedUser(userWithdrawalVo);
        return userWithdrawalVo;
    }

    @RequestMapping(value = "my-article", method = RequestMethod.GET)
    public String getMyArticle(
            @RequestAttribute(value = "userEntity", required = false)
            UserEntity userEntity,
            @ModelAttribute("params") final ArticleSearchDto params,
            Model model
    ){
        params.setUserId(userEntity.getId());
        PagingResponse<ArticleListDto> articlesResponse = this.boardService.getUserArticlesForBoardList(params);
        model.addAttribute("articlesResponse", articlesResponse);
        return "user/my-article";
    }
}
