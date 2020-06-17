package com.naver.board.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.naver.board.domain.MailVO;
import com.naver.board.domain.Member;
import com.naver.board.service.MemberService;
import com.naver.board.task.SendMail;

/*
   @Component�� �̿��ؼ� ������ �����̳ʰ� �ش� Ŭ���� ��ü�� �����ϵ��� ������ �� ������
   ��� Ŭ������ @Component�� �Ҵ��ϸ� � Ŭ������ � ������ �����ϴ��� �ľ��ϱ�
   ��ƽ��ϴ�. ������ �����ӿ�ũ������ �̷� Ŭ�������� �з��ϱ� ���ؼ�
   @Component�� ����Ͽ� ������ ���� �� ���� �ֳ����̼��� �����մϴ�.
   
   1. @Controller - ������� ��û�� �����ϴ� Controller Ŭ����
   2. @Repository - ������ ���̽� ������ ó���ϴ� DAO Ŭ����
   3. @Service - ����Ͻ� ������ ó���ϴ� Service Ŭ����
*/
@Controller
public class MemberController {

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private SendMail sendMail;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	/*
	 * @CookieValue(value="saveid", required=false) Cookie readCookie �̸��� saveid��
	 * ��Ű�� Cookie Ÿ������ ���޹޽��ϴ�. ������ �̸��� ��Ű�� �������� ���� ���� �ֱ� ������ required=false�� �����ؾ�
	 * �մϴ�. ��, id ����ϱ� ������������ ���� �ֱ� ������ required=false�� �����ؾ� �մϴ�. required=true ���¿���
	 * ������ �̸��� ���� ��Ű�� �������� ������ ������ MVC�� �ͼ����� �߻���ŵ�ϴ�.
	 */
	// �α��� �� �̵�
	@RequestMapping(value = "/login.net", method = RequestMethod.GET)
	public ModelAndView login(ModelAndView mv, @CookieValue(value = "saveid", required = false) Cookie readCookie)
			throws Exception {
		if (readCookie != null) {
			mv.addObject("saveid", readCookie.getValue());
			System.out.println("cookie time=" + readCookie.getMaxAge());
		}
		mv.setViewName("member/loginForm");
		return mv;
	}

	@GetMapping(value = "/member_delete.net")
	public String delete(ModelAndView mv, String id, HttpServletResponse response) throws Exception {
		int result = memberService.delete(id);
		if(result == 0) return null;
		return "redirect:member_list.net";
	}

	@GetMapping(value = "/logout.net")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:login.net";
	}

	// ȸ���� ��������
	@GetMapping(value = "member_info.net")
	public ModelAndView member_info(@RequestParam("id") String id, ModelAndView mv) throws Exception {
		Member m = memberService.member_info(id);
		mv.setViewName("member/member_info");
		mv.addObject("memberinfo", m);
		return mv;
	}

	@GetMapping(value = "/member_update.net")
	public ModelAndView member_update(HttpSession session, ModelAndView mv) throws Exception {
		String id = (String) session.getAttribute("id");
		Member m = memberService.member_info(id);
		mv.setViewName("member/updateForm");
		mv.addObject("memberinfo", m);
		return mv;
	}

	@RequestMapping(value = "/member_list.net")
	public ModelAndView getSearchList(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
			@RequestParam(value = "limit", defaultValue = "3", required = false) int limit,
			@RequestParam(value = "search_word", defaultValue = "", required = false) String search_word,
			@RequestParam(value = "search_field", defaultValue = "-1", required = false) int index, ModelAndView mv,
			HttpServletRequest request) {

		List<Member> list = null;
		int listcount = 0;

		// �� ����Ʈ ���� �޾ƿɴϴ�.
		listcount = memberService.getSearchCount(index, search_word);
		list = memberService.getSearchList(index, search_word, page, limit);

		int maxpage = (listcount + limit - 1) / limit;
		System.out.println("�� ������ �� =" + maxpage);

		int startpage = ((page - 1) / 10) * 10 + 1;
		int endpage = startpage + 10 - 1;
		System.out.println("���� ���������� ������ ������ ������ ��=" + endpage);
		System.out.println("���� ���������� ������ ���� ������ ��=" + startpage);

		if (endpage > maxpage)
			endpage = maxpage;
		mv.setViewName("member/member_list");
		mv.addObject("page", page);
		mv.addObject("maxpage", maxpage);
		mv.addObject("startpage", startpage);
		mv.addObject("endpage", endpage);
		mv.addObject("listcount", listcount);
		mv.addObject("memberlist", list);
		mv.addObject("limit", limit);
		mv.addObject("search_field", index);
		mv.addObject("search_word", search_word);
		return mv;
	}

	@PostMapping(value = "/updateProcess.net")
	public void updateProcess(Member member, HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		int result = memberService.update(member);
		out.println("<script>");
		if (result == 1) {
			out.println("alert('�����Ǿ����ϴ�.');");
			out.println("location.href='BoardList.bo';");
		} else {
			out.println("alert('ȸ�� ���� ���� ����');");
			out.println("history.back()");
		}
		out.println("</script>");
		out.close();
	}

	// ȸ������ �� �̵�
	@RequestMapping(value = "/join.net", method = RequestMethod.GET)
	public String join() {
		return "member/joinForm"; // WEB-INF/views/member/joinForm.jsp
	}

	// ȸ������ó��
	@RequestMapping(value = "/joinProcess.net", method = RequestMethod.POST)
	public void joinProcess(Member member, HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		String encPassword = passwordEncoder.encode(member.getPassword());
		System.out.println(encPassword);
		member.setPassword(encPassword);
		
		int result = memberService.insert(member);
		out.println("<script>");
		// ������ �� ���
		if (result == 1) {
			
			MailVO vo = new MailVO();
			vo.setTo(member.getEmail());
			vo.setContent(member.getId()+"�� ȸ�� ������ ���ϵ帳�ϴ�.");
			sendMail.sendMail(vo);
			
			out.println("alert('ȸ�������� �����մϴ�.');");
			out.println("location.href='login.net';");
		} else if (result == -1) {
			out.println("alert('���̵� �ߺ��Ǿ����ϴ�. �ٽ� �Է��ϼ���');");
			// out.println("location.href='join.net';"); //���ΰ�ħ�Ǿ� ������ �Է��� �����Ͱ� ��Ÿ���� �ʽ��ϴ�.
			out.println("history.back();"); // ��й�ȣ�� ������ �ٸ� �����ʹ� �����Ǿ� �ֽ��ϴ�.
		}
		out.println("</script>");
		out.close();
	}

	@RequestMapping(value = "/idcheck.net", method = RequestMethod.GET)
	public void idCheck(@RequestParam("id") String id, HttpServletResponse response) throws Exception {
		int result = memberService.isId(id);
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(result);
	}

	// �α��� ó��
	@RequestMapping(value = "/loginProcess.net", method = RequestMethod.POST)
	public String loginProcess(@RequestParam("id") String id, @RequestParam("password") String password,
			@RequestParam(value = "remember", defaultValue = "") String remember, HttpServletResponse response,
			HttpSession session) throws Exception {
		int result = memberService.isId(id, password);
		System.out.println("����� " + result);

		if (result == 1) { // �α��� ����
			session.setAttribute("id", id);
			Cookie savecookie = new Cookie("saveid", id);
			if (!remember.equals("")) {
				savecookie.setMaxAge(60 * 60);
				System.out.println("��Ű���� : 60*60");
			} else {
				System.out.println("��Ű���� : 0");
				savecookie.setMaxAge(0);
			}
			response.addCookie(savecookie);
			return "redirect:BoardList.bo";
		} else {
			String message = "��й�ȣ�� ��ġ���� �ʽ��ϴ�.";
			if (result == -1)
				message = "���̵� �������� �ʽ��ϴ�.";
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('" + message + "');");
			out.println("location.href='login.net';");
			out.println("</script>");
			out.close();
			return null;
		}
	}
}