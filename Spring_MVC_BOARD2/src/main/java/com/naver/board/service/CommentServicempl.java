package com.naver.board.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.naver.board.dao.CommentDAO;
import com.naver.board.domain.Comment;
import com.naver.board.domain.Member;;

@Service
public class CommentServicempl implements CommentService{

	@Autowired
	private CommentDAO dao;
	
	@Override
	public int getListCount(int board_num) {
		return dao.getListCount(board_num);
	}

	@Override
	public List<Comment> getCommentList(int board_num, int page) {
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
		return dao.commentsInsert(c);
	}

	@Override
	public int commentsDelete(int num) {
		return dao.CommentsDelete(num);
	}

	@Override
	public int commentsUpdate(Comment co) {
		return dao.commentsUpdate(co);
	}

}
