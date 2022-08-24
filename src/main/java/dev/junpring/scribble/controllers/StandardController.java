package dev.junpring.scribble.controllers;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Component
public class StandardController {

    @ExceptionHandler(value = Exception.class)
    protected ModelAndView handleException(Exception exception) {
        try {
            System.out.println(exception.getMessage());
            exception.printStackTrace();

//            this.systemService.putExceptionLog(exception);

            return new ModelAndView("error");
        } catch (Exception ignored) {
            return null;
        }
    }
}
