package com.naver.board.common;

/* joinPoint 인터페이스의 메서드
 * Signature getSignature() : 호출되는 메서드에 대한 정보를 구합니다.
 * Object getTarget() : 클라이언트가 호출한 비즈니스 메서드를 포함하는 비즈니스 객체를 구합니다.
 * Object[] getArgs() : 클라이언트가 메서드를 호출할 때 넘겨준 인자 목록을 Object 배열로 리턴합니다.
 */
public class AfterReturningAdvice {
	public void afterReturninglog(Object obj) {
		System.out.println("========================================================");
		System.out.println("[AfterReturningAdvice] obj : "+obj.toString());
		System.out.println("========================================================");
	}
}
