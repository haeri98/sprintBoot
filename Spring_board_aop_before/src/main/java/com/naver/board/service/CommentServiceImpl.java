/* ������ CommentServiceImpl Ŭ��������
 * �޼����� ���� ������ �α׸� ����ϵ��� �� ����� �ҽ��Դϴ�.
 * 
 * �� ��쿡�� COmmentServiceImpl Ŭ������ LogAdviceŬ������ ���ϰ� ���յǾ� �ֽ��ϴ�.
 * ���� LogAdvice Ŭ������ �ٸ� Ŭ������ �����ؾ� �ϰų�
 * ���� ����� �޼��� beforeLog()�� �ñ״�ó(����Ÿ��, �̸�, �Ű�����)�� ����Ǵ� ���
 * ������ �Ұ����մϴ�.
 * Advice Ŭ���� ��ü�� �����ϰ� ���� �޼��带 ȣ���ϴ� �޼��带 ȣ���ϴ� �ڵ尡
 * ����Ͻ� �ڵ忡 �ִٸ� �ٽ� ����(commentServiceImpl)�� Ⱦ�� ����(LogAdvice)��
 * �и��� �� �����ϴ�.
 */

package com.naver.board.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naver.board.aop.LogAdvice;
import com.naver.board.dao.CommentDAO;
import com.naver.board.domain.Comment;

@Service
public class CommentServiceImpl implements CommentService{
	
	@Autowired
	public LogAdvice log;

	@Autowired
	private CommentDAO dao;
	
	@Override
	public int getListCount(int board_num) {
		log.beforeLog();
		return dao.getListCount(board_num);
	}

	@Override
	public List<Comment> getCommentList(int board_num, int page) {
		log.beforeLog();
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		int startrow = (page - 1) * 3 + 1;
		int endrow = startrow + 3 - 1;
		
		map.put("board_num", board_num);
		map.put("page",page);
		map.put("start", startrow);
		map.put("end", endrow);
		return dao.getCommentList(map);
	}

	@Override
	public int commentsInsert(Comment c) {
		log.beforeLog();
		return dao.commentsInsert(c);
	}

	@Override
	public int commentsDelete(int num) {
		log.beforeLog();
		return dao.CommentsDelete(num);
	}

	@Override
	public int commentsUpdate(Comment co) {
		log.beforeLog();
		return dao.commentsUpdate(co);
	}

}
