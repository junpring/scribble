package dev.junpring.scribble.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component("beforeActionInterceptor") // 다른 컴포넌트 식별을 위해 이름을 붙임.
@Slf4j
public class BeforeActionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

/*        log.info("request.getRequestURI() : " + request.getRequestURI());
        if (request.getRequestURI().equals("/resources/stylesheets/common.css")){
            return false;
*/
        Map<String, Object> param = new HashMap<>();

        Enumeration<String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            Object paramValue = request.getParameter(paramName);
            param.put(paramName, paramValue);
        }

        ObjectMapper mapper = new ObjectMapper();
        String paramJson = mapper.writeValueAsString(param);
        String paramKeyword = request.getParameter("keyword");

        request.setAttribute("paramKeyword", paramKeyword);
        request.setAttribute("param", param);
        request.setAttribute("paramJson", paramJson);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
