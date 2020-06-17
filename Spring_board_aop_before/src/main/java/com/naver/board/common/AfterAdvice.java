package com.naver.board.common;

import org.aspectj.lang.JoinPoint;

/* 호출되는 비즈니스 메서드의 정보를 JoinPoint 인터페이스로 알 수 있습니다
 * 
 * JoinPoint 인터페이스 메서드
 * 1. Signature getSingnature() : 호출되는 메서드에 대한 정보를 구합니다.
 * 2. Object getTarget() : 호출한 비즈니스 메서드를 포함하는 비즈니스 객체를 구합니다.
 * 3. getClass().getName(): 클래스 이름을 구합니다.
 * 4. proceeding.getSignature().getName() : 호출되는 메서드 이름을 구합니다.
 * 
 * 공통으로 처리할 로직을 BeforeAdvice 클래스에 beforeLog()메서드로 구현합니다.
 * advice : 횡단 관심에 해당하는 공통 기능을 의미하며 독립된 클래스의 메서드로 작성되니다.
 */
public class AfterAdvice {
	public void afterlog(JoinPoint proceeding) {
		/* 출력 내용
		 * [AfterAdvice] : 비즈니스 로직 수행 전 동작입니다.
		 * [AfterAdvice] : com.naver.board.service.BoardServiceImpl의 getBoardList 호출합니다.
		 */
		System.out.println("========================================================");
		System.out.println("[AfterAdvice] : 비즈니스 로직 수행 후 동작입니다.");
		System.out.println("[AfterAdvice] : " + proceeding.getTarget().getClass().getName() +
				"의 "+proceeding.getSignature().getName()+" 호출 후 입니다.");
		System.out.println("========================================================");
	}
}
