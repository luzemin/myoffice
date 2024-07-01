package com.myoffice.app.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = Logger.getLogger(getClass());

    @ExceptionHandler(value = Exception.class)
    public R handleSpecificExceptions(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        logger.error("system error:");
        logger.error("request url:" + request.getRequestURI());
        logger.error("exception:" + ex);

        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return R.fatal("internal server error");
    }
}
