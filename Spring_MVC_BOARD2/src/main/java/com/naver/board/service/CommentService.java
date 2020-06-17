package com.naver.board.service;

import java.util.List;

import com.naver.board.domain.Comment;

public interface CommentService {

	public int getListCount(int num);	// 글의 갯수 구하기
	public List<Comment> getCommentList(int board_num, int page); // 댓글 목록 가져오기
	public int commentsInsert(Comment c); // 댓글 등록하기
	public int commentsDelete(int num); // 댓글 삭제
	public int commentsUpdate(Comment co); // 댓글 수정

}
