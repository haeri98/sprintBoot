package com.naver.board.aop;

import org.springframework.stereotype.Component;

// �������� ó���� ������ logAdvice Ŭ������ beforeLog() �޼���� ����
@Component
public class LogAdvice {
	//LogAdvice Ŭ������ ���� �޼��带 aop���� advice��� �մϴ�.
	public void beforeLog() {
		System.out.println("======> LogAdvice : �����Ͻ� ���� ���� �� �����Դϴ�.");
	}
}
