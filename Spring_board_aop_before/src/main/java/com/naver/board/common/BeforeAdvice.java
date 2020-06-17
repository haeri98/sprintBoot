package com.naver.board.common;

import org.aspectj.lang.JoinPoint;

/* ȣ��Ǵ� ����Ͻ� �޼����� ������ JoinPoint �������̽��� �� �� �ֽ��ϴ�
 * 
 * JoinPoint �������̽� �޼���
 * 1. Signature getSingnature() : ȣ��Ǵ� �޼��忡 ���� ������ ���մϴ�.
 * 2. Object getTarget() : ȣ���� ����Ͻ� �޼��带 �����ϴ� ����Ͻ� ��ü�� ���մϴ�.
 * 3. getClass().getName(): Ŭ���� �̸��� ���մϴ�.
 * 4. proceeding.getSignature().getName() : ȣ��Ǵ� �޼��� �̸��� ���մϴ�.
 * 
 * �������� ó���� ������ BeforeAdvice Ŭ������ beforeLog()�޼���� �����մϴ�.
 * advice : Ⱦ�� ���ɿ� �ش��ϴ� ���� ����� �ǹ��ϸ� ������ Ŭ������ �޼���� �ۼ��Ǵϴ�.
 */
public class BeforeAdvice {
	public void beforeLog(JoinPoint proceeding) {
		/* ��� ����
		 * [BeforeAdvice] : ����Ͻ� ���� ���� �� �����Դϴ�.
		 * [BeforeAdvice] : com.naver.board.service.BoardServiceImpl�� getBoardList ȣ���մϴ�.
		 */
		System.out.println("========================================================");
		System.out.println("[BeforeAdvice] : ����Ͻ� ���� ���� �� �����Դϴ�.");
		System.out.println("[BeforeAdvice] : " + proceeding.getTarget().getClass().getName() +
				"�� "+proceeding.getSignature().getName()+" ȣ���մϴ�.");
		System.out.println("========================================================");
	}
}
