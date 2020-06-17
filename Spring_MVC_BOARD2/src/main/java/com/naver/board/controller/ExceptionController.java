package com.naver.board.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

/* ControllerAdvice 애노테이션을 통해서 이 클래스의 객체가
 * 컨트롤러에서 발생하는 Exception을 전문적으로 처리하는 클래스하는것을 명시한다.
 * 
 * 만드는 방식
 * 1. 클래스에 @ControllerAdvice라는 애노테이션 처리
 * 2. 각 메소드에 @ExceptionHandler를 이용해서 적절한 타입의 Exception을 처리
 */

@ControllerAdvice
public class ExceptionController {
	private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class);
	
	@ExceptionHandler(NoHandlerFoundException.class)
	public ModelAndView handleError404(HttpServletRequest request, Exception e) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("error/404");
		mav.addObject("exception","404오류");
		mav.addObject("url", request.getRequestURL());
		return mav;
	}
	
	// common메소드는 Exception 타입으로 처리하는 모든 예외를 처리하도록 설정
	@ExceptionHandler(Exception.class)
	public ModelAndView common(Exception e, HttpServletRequest request) {
		logger.info(e.toString());
		ModelAndView mav = new ModelAndView();
		mav.setViewName("error/common");
		mav.addObject("exception", e);
		mav.addObject("url", request.getRequestURL());
		return mav;
	}
}
