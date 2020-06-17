package com.naver.board.common;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.springframework.util.StopWatch;

public class AroundAdvice {
	/* PreceddingJoinPoint �������̽��� JoinPoint�� ����߱� ������
	 * JoinPoint�� ���� ��� �޼��带 �����մϴ�.
	 * Around Advice������ ProceddingJoinPoint�� �Ű������� ����ϴµ�
	 * �� ������ proceed()�޼��尡 �ʿ��ϱ� �����Դϴ�.
	 * Around Advice�� ��� ����Ͻ� �޼��� ���� ���� �Ŀ� ����Ǵ� �� 
	 * ����Ͻ� �޼��带 ȣ���ϱ����ؼ��� ProceedingPoint�� proceed()�޼��尡 �ʿ��մϴ�
	 * ��, Ŭ���̾�Ʈ�� ��û�� ����æ �����̽���
	 * Ŭ���̾�Ʈ�� ȣ���� ����Ͻ� �޼���(ServiceImpl�� get���� �����ϴ� �޼���)�� ȣ���ϱ� ����
	 * ProceedingJoinPoint ��ü�� �Ű� ������ �޾� proceed() �޼��带 ���ؼ�
	 * ����Ͻ� �޼��带 ȣ���� �� �ֽ��ϴ�.
	 * proceed()�޼��� ���� �� �޼ҵ��� ��ȯ���� �����ؾ��մϴ�.
	 */
	public Object aroundLog(ProceedingJoinPoint proceeding) throws Throwable {
		System.out.println("======================================================");
		System.out.println("[Around Advice�� before]: ����Ͻ� �޼��� ���� ���Դϴ�.");
		StopWatch sw = new StopWatch();
		sw.start();
		
		// �� �ڵ��� ������ ���Ŀ� ���� ����� ���� �ڵ带 ��ġ ��Ű�� �˴ϴ�.
		// ��� ��ü�� �޼��� BoardServiceImpl.getListCount([]) �� ȣ���մϴ�.
		Object result = proceeding.proceed();
		sw.stop();
		
		System.out.println("[Around advice�� after]: ����Ͻ� �޼��� ���� �� �Դϴ�.");
		Signature sig = proceeding.getSignature();
		
		//Object[] getArgs() : Ŭ���̾�Ʈ�� �޼��带 ȣ���� �� �Ѱ��� ���� ����� object �迭�� ����
		System.out.printf("[Around advice�� after]: %s.%s(%s) \n", 
				proceeding.getTarget().getClass().getSimpleName(),sig.getName(),
				Arrays.deepToString(proceeding.getArgs()));
		System.out.println("[Around advice�� after]: "+
				proceeding.getSignature().getName()+"()�޼ҵ� ���� �ð� : "+
				sw.getTotalTimeMillis()+"(ms)��");
		System.out.println("[Around Advice�� after]: "+
				proceeding.getTarget().getClass().getName());
		System.out.println("proceeding.proceed() ���� �� ��ȯ�� : "+result);
		System.out.println("======================================================");
		return result;
	}
}
