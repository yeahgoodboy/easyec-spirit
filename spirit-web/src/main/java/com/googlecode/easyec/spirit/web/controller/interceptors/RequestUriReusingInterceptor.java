package com.googlecode.easyec.spirit.web.controller.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 设置请求的URL为可重用的拦截器。
 *
 * @author JunJie
 */
public class RequestUriReusingInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(RequestUriReusingInterceptor.class);
    public static final String THIS_REQUEST_URI = "com.easycommerce.web.REQUEST_URI";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ((handler instanceof Controller) || handler.getClass().isAnnotationPresent(org.springframework.stereotype.Controller.class)) {
            String requestUri = request.getRequestURI().replaceFirst(request.getContextPath(), "");
            logger.debug("This uri is: [" + requestUri + "].");

            request.setAttribute(THIS_REQUEST_URI, requestUri);
        }

        return true;
    }
}
