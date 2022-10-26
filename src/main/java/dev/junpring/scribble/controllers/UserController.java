package dev.junpring.scribble.controllers;

import dev.junpring.scribble.entities.member.UserEntity;
import dev.junpring.scribble.enums.member.user.LoginResult;
import dev.junpring.scribble.entities.member.SessionEntity;
import dev.junpring.scribble.services.UserService;
import dev.junpring.scribble.vos.member.user.UserEmailVerifyCodeVo;
import dev.junpring.scribble.vos.member.user.UserLoginVo;
import dev.junpring.scribble.vos.member.user.UserRegisterVo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller(value ="dev.junpring.scribble.controllers.UserController")
@RequestMapping(value = "/user")
public class UserController {
    private final UserService userService;
    private static final int DEFAULT_LEVEL = 9; // 기본값 레벨(9) : 일반 유저

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "check-email", method = RequestMethod.POST)
    @ResponseBody
    public String postEmailCheck(
            UserEntity userEntity
    ) {
        int emailCount = this.userService.CountByEmail(userEntity);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return String.valueOf(emailCount);
    }

    @RequestMapping(value = "check-nickname", method = RequestMethod.POST)
    @ResponseBody
    public String postNicknameCheck(
            UserEntity userEntity
    ) {
        int nicknameCount = this.userService.CountByNickname(userEntity);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
    )throws MessagingException {
        registerVo.setLevel(DEFAULT_LEVEL);
        registerVo.setId(0);
        registerVo.setDeleted(false);
        registerVo.setSuspended(false);
        registerVo.setEmailVerified(false);
        registerVo.setResult(null);
        this.userService.register(registerVo);
        JSONObject obj = new JSONObject();
        obj.put("result", registerVo.getResult().name());
        System.out.println("getResult: " + registerVo.getResult().name());
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
        System.out.println("verify result: " + userEmailVerifyCodeVo.getResult());
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
            HttpServletResponse response
    ){
        loginVo.setResult(null);
        this.userService.login(loginVo, request);
        if (loginVo.getResult() == LoginResult.SUCCESS) {
            Cookie sessionKeyCookie = new Cookie("sk", loginVo.getSessionEntity().getKey());
            // new 쿠키생성("이름", service에서 sessionKey 해싱된 키)
            sessionKeyCookie.setPath("/");
            // 위 설정하지않으면 기본 path : 현재주소 (/user/**) 이 주소에만 사용가능, /는 전역 사용가능.
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
    ){
        SessionEntity sessionEntity = null;
        if (request.getAttribute("sessionEntity") instanceof SessionEntity) {
            sessionEntity = (SessionEntity) request.getAttribute("sessionEntity");
            this.userService.expireSession(sessionEntity);
        }
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }
}
