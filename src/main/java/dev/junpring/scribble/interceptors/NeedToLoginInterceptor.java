package dev.junpring.scribble.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component("needToLoginInterceptor")
public class NeedToLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getAttribute("userEntity") == null) {
            if (!"XMLHttpRequest".equals(request.getHeader("x-requested-with"))) {
                response.setContentType("text/html; charset=UTF-8");
                response.getWriter().append("<script>");
                response.getWriter().append("alert('로그인 후 이용해주세요.');");
                response.getWriter().append("location.replace('/user/login');");
                response.getWriter().append("</script>");
            }
            else {
                response.setContentType("application/json; charset=UTF-8");
                response.getWriter().append("{\"resultCode\":\"F-A\",\"msg\":\"로그인 후 이용해주세요.\"}");
            }
            return false;
        }
        return true;
    }
}
