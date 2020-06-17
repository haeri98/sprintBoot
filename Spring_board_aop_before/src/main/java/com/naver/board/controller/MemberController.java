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
   @Component를 이용해서 스프링 컨테이너가 해당 클래스 객체를 생성하도록 설정할 수 있지만
   모든 클래스에 @Component를 할당하면 어떤 클래스가 어떤 역할을 수행하는지 파악하기
   어렵습니다. 스프링 프레임워크에서는 이런 클래스들을 분류하기 위해서
   @Component를 상속하여 다음과 같은 세 개의 애노테이션을 제공합니다.
   
   1. @Controller - 사용자의 요청을 제어하는 Controller 클래스
   2. @Repository - 데이터 베이스 연동을 처리하는 DAO 클래스
   3. @Service - 비즈니스 로직을 처리하는 Service 클래스
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
	 * @CookieValue(value="saveid", required=false) Cookie readCookie 이름이 saveid인
	 * 쿠키를 Cookie 타입으로 전달받습니다. 지정한 이름의 쿠키가 존재하지 않을 수도 있기 때문에 required=false로 설정해야
	 * 합니다. 즉, id 기억하기 선택하지않을 수도 있기 때문에 required=false로 설정해야 합니다. required=true 상태에서
	 * 지정한 이름을 가진 쿠키가 존재하지 않으면 스프링 MVC는 익셉션을 발생시킵니다.
	 */
	// 로그인 폼 이동
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

	// 회원의 개인정보
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

		// 총 리스트 수를 받아옵니다.
		listcount = memberService.getSearchCount(index, search_word);
		list = memberService.getSearchList(index, search_word, page, limit);

		int maxpage = (listcount + limit - 1) / limit;
		System.out.println("총 페이지 수 =" + maxpage);

		int startpage = ((page - 1) / 10) * 10 + 1;
		int endpage = startpage + 10 - 1;
		System.out.println("현재 페이지에서 보여줄 마지막 페이지 수=" + endpage);
		System.out.println("현재 페이지에서 보여줄 시작 페이지 수=" + startpage);

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
			out.println("alert('수정되었습니다.');");
			out.println("location.href='BoardList.bo';");
		} else {
			out.println("alert('회원 정보 수정 실패');");
			out.println("history.back()");
		}
		out.println("</script>");
		out.close();
	}

	// 회원가입 폼 이동
	@RequestMapping(value = "/join.net", method = RequestMethod.GET)
	public String join() {
		return "member/joinForm"; // WEB-INF/views/member/joinForm.jsp
	}

	// 회원가입처리
	@RequestMapping(value = "/joinProcess.net", method = RequestMethod.POST)
	public void joinProcess(Member member, HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		String encPassword = passwordEncoder.encode(member.getPassword());
		System.out.println(encPassword);
		member.setPassword(encPassword);
		
		int result = memberService.insert(member);
		out.println("<script>");
		// 삽입이 된 경우
		if (result == 1) {
			
			MailVO vo = new MailVO();
			vo.setTo(member.getEmail());
			vo.setContent(member.getId()+"님 회원 가입을 축하드립니다.");
			sendMail.sendMail(vo);
			
			out.println("alert('회원가입을 축하합니다.');");
			out.println("location.href='login.net';");
		} else if (result == -1) {
			out.println("alert('아이디가 중복되었습니다. 다시 입력하세요');");
			// out.println("location.href='join.net';"); //새로고침되어 이전에 입력한 데이터가 나타나지 않습니다.
			out.println("history.back();"); // 비밀번호를 제외한 다른 데이터는 유지되어 있습니다.
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

	// 로그인 처리
	@RequestMapping(value = "/loginProcess.net", method = RequestMethod.POST)
	public String loginProcess(@RequestParam("id") String id, @RequestParam("password") String password,
			@RequestParam(value = "remember", defaultValue = "") String remember, HttpServletResponse response,
			HttpSession session) throws Exception {
		int result = memberService.isId(id, password);
		System.out.println("결과는 " + result);

		if (result == 1) { // 로그인 성공
			session.setAttribute("id", id);
			Cookie savecookie = new Cookie("saveid", id);
			if (!remember.equals("")) {
				savecookie.setMaxAge(60 * 60);
				System.out.println("쿠키저장 : 60*60");
			} else {
				System.out.println("쿠키저장 : 0");
				savecookie.setMaxAge(0);
			}
			response.addCookie(savecookie);
			return "redirect:BoardList.bo";
		} else {
			String message = "비밀번호가 일치하지 않습니다.";
			if (result == -1)
				message = "아이디가 존재하지 않습니다.";
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