package com.naver.board.common;

/* joinPoint �������̽��� �޼���
 * Signature getSignature() : ȣ��Ǵ� �޼��忡 ���� ������ ���մϴ�.
 * Object getTarget() : Ŭ���̾�Ʈ�� ȣ���� ����Ͻ� �޼��带 �����ϴ� ����Ͻ� ��ü�� ���մϴ�.
 * Object[] getArgs() : Ŭ���̾�Ʈ�� �޼��带 ȣ���� �� �Ѱ��� ���� ����� Object �迭�� �����մϴ�.
 * 
 * advice : Ⱦ�� ���ɿ� �ش��ϴ� ���� ����� �ǹ��ϸ� ������ Ŭ������ �޼���� �ۼ��˴ϴ�.
 * AfterThrowing(���� �߻����� �� ����)
 * Ÿ�� �޼ҵ尡 ���� �� ���ܸ� ������ �Ǹ� �����̽� ����� ����
 * BoardServiceImpl.java���� getBoardList()�ȿ�
 * double i = 1/0;
 * �߰��մϴ�.
 */
public class AfterThrowingAdvice {
	public void afterThrowinglog(Throwable exp) {
		System.out.println("========================================================");
		System.out.println("[AfterThrowing] : ����Ͻ� ���� ���� �� ������ �߻��ϸ� �����Դϴ�.");
		System.out.println("ex : "+exp.toString());
		System.out.println("========================================================");
	}
}
