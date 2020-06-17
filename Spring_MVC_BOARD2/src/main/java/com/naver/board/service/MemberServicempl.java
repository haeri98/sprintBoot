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
		int result=-1; // ���̵� �������� �ʴ� ��� -remember�� null�� ���
		if(remember != null) { // ���̵� �����ϴ� ���
			// passwordEncoder.matches(rawPassword, encodedPassword)
			// ����ڿ��� �Է¹��� �н����带 ���ϰ��� �� �� ����ϴ� �޼���
			// rawPassword : ����ڰ� �Է��� �н�����
			// encodedPassword: DB�� ����� �н�����
			if(passwordEncoder.matches(password, remember.getPassword())) { // ���̵� ��� ��ġ
				result = 1;
			}else result=0; // ���̺� ���� ��ι�ȣ ����ġ
		}
		return result;
	}
	
	// �ߺ� ���̵� �˻�
	@Override
	public int isId(String id) {
		Member member = dao.isId(id);
		return (member == null) ? -1 : 1; //-1: ���̵� �������� �ʴ°�� / 1: �Ƶ� ����
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
