package com.naver.board.task;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.naver.board.service.BoardService;

@Service
public class FileCheckTask {
	@Value("${savefoldername}")
	private String saveFolder;
	
	@Autowired
	private BoardService boardService;
	
	// cron����
	// seconds(��) minutes(��) hours(��:0~23) day(��:1~31)
	// months(��:1~12) day of week (����:0~6) year(optional)
	
	@Scheduled(cron="0 15 14 * * *")
	public void checkFiles() throws Exception {
		System.out.println("cehckFiles");
		List<String> deleteFileList = boardService.getDeleteFileList();
		
		for(int i=0; i<deleteFileList.size(); i++) {
			String filename = deleteFileList.get(i);
			File file = new File(saveFolder+filename);
			if(file.exists()) {
				if(file.delete()) {
					System.out.println(file.getPath()+"�����Ǿ����ϴ�.");
				}
			} else {
				System.out.println(file.getPath()+"������ �������� �ʽ��ϴ�.");
			}
		}
	}
	
	// �����ٷ��� �̿��ؼ� �ֱ������� ����, ����, �ſ� ���α׷� ������ ���� �۾��� �ǽ�
	//@Scheduled(fixedDelay=1000) // ������ ����� Task ���� �ð����κ��� ���ǵ� �ð���ŭ ���� �� Task�� ����
	// �и������� ����
	public void test() throws Exception {
		System.out.println("test");
	}
}
