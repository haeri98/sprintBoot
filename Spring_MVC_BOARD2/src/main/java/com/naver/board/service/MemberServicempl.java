package com.naver.board.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.naver.board.dao.MemberDAO;
import com.naver.board.domain.Board;
import com.naver.board.domain.Member;

@Service
public class MemberServicempl implements MemberService{
	@Autowired
	private MemberDAO dao;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public int insert(Member m) {
		return dao.insert(m);
	}
	
	@Override
	public int isId(String id, String password) {
		Member remember = dao.isId(id);
		int result=-1; // 아이디가 존재하지 않는 경우 -remember가 null인 경우
		if(remember != null) { // 아이디가 존재하는 경우
			// passwordEncoder.matches(rawPassword, encodedPassword)
			// 사용자에게 입력받은 패스워드를 비교하고자 할 때 사용하는 메서드
			// rawPassword : 사용자가 입력한 패스워드
			// encodedPassword: DB에 저장된 패스워드
			if(passwordEncoder.matches(password, remember.getPassword())) { // 아이디 비번 일치
				result = 1;
			}else result=0; // 아이비 존재 비민번호 불일치
		}
		return result;
	}
	
	// 중복 아이디 검사
	@Override
	public int isId(String id) {
		Member member = dao.isId(id);
		return (member == null) ? -1 : 1; //-1: 아이디가 존재하지 않는경우 / 1: 아디가 존재
	}

	@Override
	public Member member_info(String id) {
		return dao.member_info(id);
	}

	@Override
	public int delete(String id) {
		return dao.delete(id);
	}

	@Override
	public int update(Member m) {
		return dao.update(m);
	}

	@Override
	public List<Member> getSearchList(int index, String search_word, int page, int limit) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(index != -1) {
			String[] search_field= new String[] {"id","name","age","gender"};
			map.put("field", search_field[index]);
			map.put("value", "%"+search_word+"%");
		}
		int startrow = (page - 1) * limit + 1;
		int endrow = startrow + limit - 1;
		map.put("start", startrow);
		map.put("end", endrow);
		return dao.getSearchList(map);
	}

	@Override
	public int getSearchCount(int index, String search_word) {
		HashMap<String, String> map = new HashMap<String, String>();
		if(index != -1) {
			String[] search_field= new String[] {"id","name","age","gender"};
			map.put("field", search_field[index]);
			map.put("value", "%"+search_word+"%");
		}
		return dao.getSearchCount(map);
	} 
}
