package com.naver.board.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.naver.board.domain.Board;

@Repository
public class BoardDAO {

	@Autowired
	private SqlSessionTemplate sqlSession;

	public int getListCount() {
		return sqlSession.selectOne("Boards.count");
	}

	public List<Board> getBoardList(HashMap<String, Integer> map) {
		return sqlSession.selectList("Boards.list", map);
	}

	public void insertBoard(Board board) {
		sqlSession.insert("Boards.insert", board);
	}

	public int setReadCountUpdate(int number) {
		return sqlSession.update("Boards.ReadCountUpdate", number);
	}

	public Board isBoardWriter(Map<String, Object> map) {
		return sqlSession.selectOne("Boards.BoardWriter", map);
	}
	
	public int boardModify(Board modifyboard) {
		return sqlSession.update("Boards.modify", modifyboard);
	}

	public Board getDetail(int number) {
		return sqlSession.selectOne("Boards.detail", number);
	}

	public int boardReplyUpdate(Board board) {
		// 매퍼 파일들 중에 아이디가 하나만 있는경우 매퍼 파일의 namespace 생략 가능
		return sqlSession.update("reply_update",board);
	}

	public int boardReply(Board board) {
		return sqlSession.insert("Boards.reply_insert",board);
	}

	public int boardDelete(Board board) {
		return sqlSession.delete("Boards.delete",board);
	}

	public int insert_deleteFile(String before_file) {
		return sqlSession.insert("Boards.insert_deleteFile",before_file);
	}

	public int insert_deleteFiles(Board board) {
		return sqlSession.insert("Boards.insert_deleteFiles",board);
		
	}

	public List<String> getDeleteFileList() {
		return sqlSession.selectList("Boards.deleteFileList");
	}

}