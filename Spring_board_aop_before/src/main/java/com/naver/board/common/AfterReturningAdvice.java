package com.naver.board.common;

/* joinPoint �������̽��� �޼���
 * Signature getSignature() : ȣ��Ǵ� �޼��忡 ���� ������ ���մϴ�.
 * Object getTarget() : Ŭ���̾�Ʈ�� ȣ���� ����Ͻ� �޼��带 �����ϴ� ����Ͻ� ��ü�� ���մϴ�.
 * Object[] getArgs() : Ŭ���̾�Ʈ�� �޼��带 ȣ���� �� �Ѱ��� ���� ����� Object �迭�� �����մϴ�.
 */
public class AfterReturningAdvice {
	public void afterReturninglog(Object obj) {
		System.out.println("========================================================");
		System.out.println("[AfterReturningAdvice] obj : "+obj.toString());
		System.out.println("========================================================");
	}
}
