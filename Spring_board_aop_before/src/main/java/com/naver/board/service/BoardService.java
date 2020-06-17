package com.naver.board.service;

import java.util.List;

import com.naver.board.domain.Board;

public interface BoardService {
	public List<Board> getBoardList(int page, int limit);
	public int getListCount();
	public Board getDetail(int num);
	public int boardReply(Board board);
	public int boardModify(Board modifyboard);
	public int boardDelete(int num);
	public void insertBoard(Board board);
	public boolean isBoardWriter(int num, String pass);
	public int boardReplyUpdate(Board board);
	public int setReadCountUpdate(int num);
	public int insert_deleteFile(String before_file);
	public List<String> getDeleteFileList();
}
