package dev.junpring.scribble.services;

import dev.junpring.scribble.entities.member.UserEmailVerifyCodeEntity;
import dev.junpring.scribble.entities.member.UserEntity;
import dev.junpring.scribble.enums.member.user.LoginResult;
import dev.junpring.scribble.enums.member.user.RegisterResult;
import dev.junpring.scribble.enums.member.user.UserEmailVerifyResult;
import dev.junpring.scribble.utills.CryptoUtil;
import dev.junpring.scribble.entities.member.SessionEntity;
import dev.junpring.scribble.mappers.IUserMapper;
import dev.junpring.scribble.vos.member.user.UserEmailVerifyCodeVo;
import dev.junpring.scribble.vos.member.user.UserLoginVo;
import dev.junpring.scribble.vos.member.user.UserRegisterVo;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service(value = "dev.junpring.scribble.services.UserService")
public class UserService {

    public static final int CODE_VALID_MINUTES = 30; // 코드 유효시간
    public static final int CODE_HASH_ITERATION_COUNT = 10; // 코드 해시 반복횟수
    public static final int SALT_HASH_ITERATION_COUNT = 20; // 솔트 해시 반복횟수

    public static boolean checkEmail(String input) {
        return input != null && input.matches("^(?=.{10,100}$)([0-9a-z][0-9a-z_]*[0-9a-z])@([0-9a-z][0-9a-z\\-]*[0-9a-z]\\.)?([0-9a-z][0-9a-z\\-]*[0-9a-z])\\.([a-z]{2,15})(\\.[a-z]{2})?$");
        // matches : 정규식과 문자열 일치 확인
    }

    public static boolean checkPassword(String input) {
        return input != null && input.matches("^([0-9a-zA-Z`~!@#$%^&*()\\-_=+\\[{\\]}\\\\|;:'\",<.>/?]{8,100})$");
    }

    public static boolean checkNickname(String input) {
        return input != null && input.matches("^([0-9a-zA-Z가-힣]{2,10})$");
    }

    public static boolean checkContact(String input) {
        return input != null && input.matches("^([0-9]{11})$");
    }

    private final IUserMapper userMapper;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;

    @Autowired
    public UserService(IUserMapper userMapper, JavaMailSender javaMailSender, SpringTemplateEngine springTemplateEngine) {
        this.userMapper = userMapper;
        this.javaMailSender = javaMailSender;
        this.springTemplateEngine = springTemplateEngine;
    }

    public int CountByEmail(UserEntity userEntity) {
        if (!checkEmail(userEntity.getEmail())) {
            return -1;
        }
        int emailCount = this.userMapper.selectUserCountByEmail(userEntity.getEmail()); // 중복이 없으면 0
        return emailCount;
    } // email 중복체크

    public int CountByNickname(UserEntity userEntity) {
        if (!checkNickname(userEntity.getNickname())) {
            return -1;
        }
        int nicknameCount = this.userMapper.selectUserCountByNickname(userEntity.getNickname());
        return nicknameCount;
    } // nickname 중복체크

    public void extendSession(SessionEntity sessionEntity) { // 세션 연장
        sessionEntity.setUpdatedAt(new Date());
        sessionEntity.setExpiresAt(DateUtils.addMinutes(sessionEntity.getUpdatedAt(), 30));
        this.userMapper.updateSession(sessionEntity);
    }

    public SessionEntity getSession(String key) { // select key가 동일하면 세션 데이터 가져오기
        return this.userMapper.selectSessionKey(key);
    }

    // UserEntity에 저장된 이메일을 select (SessionEntity에 저장된 유저email과 동일한지 확인하기 위해서)
    public UserEntity getUserEmail(String email) {
        return this.userMapper.selectUserEmail(email);
    }
    public UserEntity getUserId(int id) {
        return this.userMapper.selectUserById(id);
    }

    public void expireSession(SessionEntity sessionEntity) {
        sessionEntity.setExpired(true); // true: 세션만료 , 로그아웃에 사용할 메서드
        this.userMapper.updateSession(sessionEntity);
    }

    public void login(UserLoginVo loginVo, HttpServletRequest request) {
        // 로그인 데이터가 정규식이 올바른지 확인
        if (!UserService.checkEmail(loginVo.getEmail()) ||
                !UserService.checkPassword(loginVo.getPassword())) {
            loginVo.setResult(LoginResult.ILLEGAL);
            return;
        }

        // password hash
        String hashedPassword = CryptoUtil.hashSha512(loginVo.getPassword());
        loginVo.setPassword(hashedPassword);

        // return type userEntity
        UserEntity userEntity = this.userMapper.selectUser(loginVo);

        // 로그인 데이터가 정확하게 들어오지 않는 경우
        if (userEntity == null || userEntity.getId() == 0) {
            loginVo.setResult(LoginResult.FAILURE);
            return;
        }

        // 유저가 삭제된 경우
        if (userEntity.isDeleted()) {
            loginVo.setResult(LoginResult.DELETED);
            return;
        }
        // 유저가 정지된 경우
        if (userEntity.isSuspended()) {
            loginVo.setResult(LoginResult.SUSPENDED);
            return;
        }
        // 유저가 이메일 인증하지 않은 경우
        if (!userEntity.isEmailVerified()) {
            loginVo.setResult(LoginResult.EMAIL_NOT_VERIFIED);
            return;
        }

        /** SELECT한 값을 보이는데로 userEntity에 담았으니 userEntity에 담긴 SELECT한 값을 매개변수 loginVo로 넘겨줘야한다.**/
        loginVo.setId(userEntity.getId());
        loginVo.setEmail(userEntity.getEmail());
        loginVo.setPassword(userEntity.getPassword());
        loginVo.setNickname(userEntity.getNickname());
        loginVo.setContact(userEntity.getContact());
        loginVo.setLevel(userEntity.getLevel());
        loginVo.setAddressPostal(userEntity.getAddressPostal());
        loginVo.setAddressPrimary(userEntity.getAddressPrimary());
        loginVo.setAddressSecondary(userEntity.getAddressSecondary());
        loginVo.setRegisteredAt(userEntity.getRegisteredAt());
        loginVo.setModifiedAt(userEntity.getModifiedAt());
        loginVo.setDeleted(userEntity.isDeleted());
        loginVo.setSuspended(userEntity.isSuspended());

        // session 만료 처리 , expired_flag == true : 만료
        this.userMapper.updateSessionExpired(loginVo.getId());

        /**      1. String.format 이용해서 %s에 (년도,월,일,시간,분,초,밀리초), email, password 담음
         2. %f에 Math.random()으로 랜덤 실수값 담음.
         3. String 변수 sessionKey에 담음.
         */
        String sessionKey = String.format("%s%s%s%f%f",
                new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()),
                loginVo.getEmail(),
                loginVo.getPassword(),
                Math.random(),
                Math.random());

        /** 브라우져 정보 가져오기 (HttpServletRequest 객체 선언 필요) => userAgent변수에 담음 */
        String userAgent = request.getHeader("user-Agent");

        /** sessionKey, userAgent 키 코드를 해싱해서 마무리. */
        sessionKey = CryptoUtil.hashSha512(sessionKey);
        userAgent = CryptoUtil.hashSha512(userAgent);

        SessionEntity sessionEntity = new SessionEntity(); // (*) 데이터를 담을 sessionEntity 객체생성
        sessionEntity.setCreatedAt(new Date()); // 세션 생성시간
        sessionEntity.setUpdatedAt(sessionEntity.getCreatedAt()); // 세션 생성시간 업데이트

        // 세션 만료시간, DateUtils.addMinutes : (세션 생성시간 + 30분)
        sessionEntity.setExpiresAt(DateUtils.addMinutes(sessionEntity.getCreatedAt(), 30));
        sessionEntity.setExpired(false); // 세션만료 여부, 강제로 isExpired에 false 지정.

        /** 로그인된 email, index 데이터를 sessionEntity에 담는다. **/
//        sessionEntity.setUserEmail(loginVo.getEmail());
        sessionEntity.setUserId(loginVo.getId());

        /** 해싱이 마무리된 sessionKey, userAgent를 sessionEntity에 담음. */
        sessionEntity.setKey(sessionKey);
        sessionEntity.setUa(userAgent);

        this.userMapper.insertSession(sessionEntity); // (*)에서 생성하고 값을 담고 그 값을 insert
        loginVo.setSessionEntity(sessionEntity); //

        loginVo.setResult(LoginResult.SUCCESS);
    }

    public void register(UserRegisterVo registerVo) throws MessagingException {
        // 회원가입 데이터가 정규식이 올바른지 확인
        if (!UserService.checkEmail(registerVo.getEmail()) ||
                !UserService.checkNickname(registerVo.getNickname()) ||
                !UserService.checkPassword(registerVo.getPassword()) ||
                !UserService.checkContact(registerVo.getContact())) {
            registerVo.setResult(RegisterResult.ILLEGAL);
            return;
        }
        // 이메일, 닉네임, 전화번호 중복
        if (this.userMapper.selectUserCountByEmail(registerVo.getEmail()) > 0) {
            registerVo.setResult(RegisterResult.DUPLICATE_EMAIL);
            return;
        }
        if (this.userMapper.selectUserCountByNickname(registerVo.getNickname()) > 0) {
            registerVo.setResult(RegisterResult.DUPLICATE_NICKNAME);
            return;
        }
        if (this.userMapper.selectUserCountByContact(registerVo.getContact()) > 0) {
            registerVo.setResult(RegisterResult.DUPLICATE_CONTACT);
            return;
        }
        // 패스워드 해싱
        String hashedPassword = CryptoUtil.hashSha512(registerVo.getPassword());
        registerVo.setPassword(hashedPassword);
        // 회원가입 데이터가 정확하게 들어오지 않는 경우
        if (this.userMapper.insertUser(registerVo) == 0) {
            registerVo.setResult(RegisterResult.FAILURE);
            return;
        }

        Date createdAt = new Date();
        Date expiresAt = DateUtils.addMinutes(createdAt, CODE_VALID_MINUTES);
        // expiresAt: 만료날짜 , 생성시간 + 30분 뒤
        String code = String.format("%d%s%s%s%f%f",
                registerVo.getId(),
                registerVo.getEmail(),
                registerVo.getPassword(),
                new SimpleDateFormat("yyyyMMddHHmmssSSS").format(createdAt), Math.random(),
                Math.random());
        String saltA = registerVo.getEmail();
        String saltB = registerVo.getPassword();
        for (int i = 0; i < CODE_HASH_ITERATION_COUNT; i++) {
            code = CryptoUtil.hashSha512(code);
        }
        for (int i = 0; i < SALT_HASH_ITERATION_COUNT; i++) {
            saltA = CryptoUtil.hashSha512(saltA);
            saltB = CryptoUtil.hashSha512(saltB);
        }
        UserEmailVerifyCodeEntity userEmailVerifyCodeEntity = new UserEmailVerifyCodeEntity();
        userEmailVerifyCodeEntity.setCreatedAt(createdAt);
        userEmailVerifyCodeEntity.setExpiresAt(expiresAt);
        userEmailVerifyCodeEntity.setExpired(false); // 만료되면 1
        userEmailVerifyCodeEntity.setCode(code);
        userEmailVerifyCodeEntity.setSalt(String.format("%s%s", saltA, saltB));
        userEmailVerifyCodeEntity.setUserId(registerVo.getId());

        this.userMapper.insertUserEmailVerificationCode(userEmailVerifyCodeEntity);

        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        Context context = new Context();
        context.setVariable("registerVo", registerVo);
        context.setVariable("userEmailVerifyCodeEntity", userEmailVerifyCodeEntity);
        mimeMessageHelper.setSubject("[SCRIBBLE] 회원가입 인증 메일");
        mimeMessageHelper.setFrom("yojop0803@gmail.com");
        mimeMessageHelper.setTo(registerVo.getEmail());
        mimeMessageHelper.setText(this.springTemplateEngine.process("emailVerifyTemplate", context), true);
        this.javaMailSender.send(mimeMessage);
        registerVo.setResult(RegisterResult.SUCCESS);
    }

    public void verifyEmail(UserEmailVerifyCodeVo userEmailVerifyCodeVo) {
        if (userEmailVerifyCodeVo.getCode() == null || userEmailVerifyCodeVo.getSalt() == null ||
                !userEmailVerifyCodeVo.getCode().matches("^([0-9a-z]{128})$") ||
                !userEmailVerifyCodeVo.getSalt().matches("^([0-9a-z]{256})$")) {
            userEmailVerifyCodeVo.setResult(UserEmailVerifyResult.ILLEGAL);
            return;
        }
        UserEmailVerifyCodeEntity userEmailVerifyCodeEntity = this.userMapper.selectUserEmailVerificationCode(
                userEmailVerifyCodeVo.getCode(),
                userEmailVerifyCodeVo.getSalt()
        );
        if (userEmailVerifyCodeEntity == null ||
                userEmailVerifyCodeEntity.getId() == 0) {
            userEmailVerifyCodeVo.setResult(UserEmailVerifyResult.FAILURE);
            return;
        }
        if (userEmailVerifyCodeEntity.isExpired() ||
                userEmailVerifyCodeEntity.getExpiresAt().compareTo(new Date()) < 0){
            userEmailVerifyCodeVo.setResult(UserEmailVerifyResult.EXPIRED);
            return;
        }
        UserEntity userEntity = this.userMapper.selectUserById(userEmailVerifyCodeEntity.getUserId());
        String saltA = userEntity.getEmail();
        String saltB = userEntity.getPassword();

        for (int i = 0; i < SALT_HASH_ITERATION_COUNT; i++) {
            saltA = CryptoUtil.hashSha512(saltA);
            saltB = CryptoUtil.hashSha512(saltB);
        }
        if (!userEmailVerifyCodeEntity.getSalt().equals(String.format("%s%s", saltA, saltB))) {
            userEmailVerifyCodeVo.setResult(UserEmailVerifyResult.FAILURE);
            return;
        }
//        위 모두가 성사된다면.
        userEntity.setEmailVerified(true);
        userEmailVerifyCodeEntity.setExpired(true);
        this.userMapper.updateUser(userEntity);
        this.userMapper.updateUserEmailVerifyCode(userEmailVerifyCodeEntity);

        userEmailVerifyCodeVo.setResult(UserEmailVerifyResult.SUCCESS);
    }
}
