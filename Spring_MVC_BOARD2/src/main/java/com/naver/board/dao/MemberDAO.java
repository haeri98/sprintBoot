package com.naver.board.dao;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.naver.board.domain.Member;

@Repository
public class MemberDAO {

	@Autowired
	private SqlSessionTemplate sqlSession;

	public int insert(Member m) {
		return sqlSession.insert("Members.insert", m);
	}
	
	public Member isId(String id) {
		return sqlSession.selectOne("Members.idcheck",id);
	}
	
	public Member member_info(String id) {
		return sqlSession.selectOne("Members.idcheck", id);
	}

	public int update(Member m) {
		return sqlSession.update("Members.update", m);
	}

	public int delete(String id) {
		return sqlSession.delete("Members.delete", id);
	}
	
	public List<Member> getSearchList(HashMap<String, Object> map) {
		return sqlSession.selectList("Members.searchList", map);
	}

	public int getSearchCount(HashMap<String, String> map) {
		return sqlSession.selectOne("Members.searchCount",map);
	}
}
