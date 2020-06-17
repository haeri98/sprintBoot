package com.naver.board.service;

import java.util.List;

import com.naver.board.domain.Comment;

public interface CommentService {

	public int getListCount(int num);	// ���� ���� ���ϱ�
	public List<Comment> getCommentList(int board_num, int page); // ��� ��� ��������
	public int commentsInsert(Comment c); // ��� ����ϱ�
	public int commentsDelete(int num); // ��� ����
	public int commentsUpdate(Comment co); // ��� ����

}
