package com.naver.board.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.naver.board.domain.Board;
import com.naver.board.service.BoardService;
import com.naver.board.service.CommentService;;

@Controller
public class BoardController {
	@Autowired
	private BoardService boardService;

	@Autowired
	private CommentService commentService;
	
	// 속성=값 형식으로 작성
	// savefoldername=D:\\springWork\\Spring_MVC_BOARD2\\src\\main\webapp\\resources\\upload\\
	// 값을 가져오기 위해서는 속성을 이용합니다.
	@Value("${savefoldername}")
	private String saveFolder;
	
	// 글쓰기
	// @RequestMapping(value = "/BoardWrite.bo", method = RequestMethod.GET)
	@GetMapping(value = "/BoardWrite.bo")
	public String board_write() {
		return "board/qna_board_write";
	}

	// 게시판 저장
	// @RequestMapping(value = "/board_write_ok.bo", method = RequestMethod.POST)
	@PostMapping(value = "/Board_write_ok.bo")
	public String board_write_ok(Board board) {
		boardService.insertBoard(board);
		return "redirect:/BoardList.bo";
	}

	/*
	 * 스프링 컨테이너는 매개변수 BbsBean객체를 생성하고 BbsBeans객체의 setter 메서드를 호출하여 사용자 입력값을 설정합니다.
	 * 매개변수의 이름과 setter의 property가 일치하면 됩니다.
	 */
	@PostMapping(value = "/BoardAddAction.bo")
	// @RequestMapping(value = "/BoardAddAction.bo", method = Requesethod.POST)
	public String bbs_wrtie_ok(Board board, HttpServletRequest request) throws Exception {
		MultipartFile uploadfile = board.getUploadfile();
		if (!uploadfile.isEmpty()) {
			String fileName = uploadfile.getOriginalFilename(); // 원래 파일명
			board.setBOARD_ORIGINAL(fileName); // 원래 파일명 저장

			// 새로운 폴더 이름 : 오늘 년-월-일
			Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			int date = c.get(Calendar.DATE);
			//String saveFolder = request.getSession().getServletContext().getRealPath("resources") + "/upload/";
			String homedir = saveFolder + year + "-" + month + "-" + date;
			System.out.println(homedir);
			File path1 = new File(homedir);
			if (!(path1.exists())) {
				path1.mkdir(); // 새로운 폴더 생성
			}

			// 난수 발생
			Random r = new Random();
			int random = r.nextInt(100000000);

			/** 확장자 구하기 시작 **/
			int index = fileName.lastIndexOf(".");
			/*
			 * 문자열에서 특정문자열의 위치 값(index)를 반환한다. indexOf가 처음 발견되는 문자열에 대한 index를 반환하는 반면,
			 * lastIndexOf는 마지막으로 발견되는 문자열의 index를 반환합니다. (파일명에 점이 여러개 있을 경우 맨 마지막에 발견되는
			 * 문자열의 위치를 리턴합니다.)
			 */
			System.out.println("index = " + index);

			String fileExtension = fileName.substring(index + 1);
			System.out.println("fileExtension = " + fileExtension);
			/** 확장자 구하기 끝 **/

			// 새로운 파일명
			String refileName = "bbs" + year + month + date + random + "." + fileExtension;
			System.out.println("refileName = " + refileName);

			// 오라클 db에 저장될 파일명
			String fileDBName = "/" + year + "-" + month + "-" + date + "/" + refileName;
			System.out.println("fileDBName = " + fileDBName);

			// 업로드한 파일을 매개변수의 경로에 저장
			uploadfile.transferTo(new File(saveFolder + fileDBName));

			// 바뀐 파일명으로 저장
			board.setBOARD_FILE(fileDBName);
		}
		boardService.insertBoard(board); // 저장 메서드 호출
		return "redirect:BoardList.bo";
	}

	@RequestMapping(value = "/BoardList.bo")
	public ModelAndView boardList(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
			ModelAndView mv) {

		int limit = 10;

		// 총 리스트 수를 받아옵니다.
		int listcount = boardService.getListCount();

		int maxpage = (listcount + limit - 1) / limit;

		int startpage = ((page - 1) / 10) * 10 + 1;

		// endpage: 현재 페이지 그룹에서 보여줄 마지막 페이지 수([10],[20],[30] 등..)
		int endpage = startpage + 10 - 1;

		if (endpage > maxpage)
			endpage = maxpage;

		List<Board> boardlist = boardService.getBoardList(page, limit);

		mv.setViewName("board/qna_board_list");
		mv.addObject("page", page);
		mv.addObject("maxpage", maxpage);
		mv.addObject("startpage", startpage);
		mv.addObject("endpage", endpage);
		mv.addObject("listcount", listcount);
		mv.addObject("boardlist", boardlist);
		mv.addObject("limit", limit);

		return mv;
	}

	@RequestMapping(value = "/BoardDetailAction.bo", method = RequestMethod.GET)
	public ModelAndView Detail(int num, ModelAndView mv, HttpServletRequest request) {

		Board board = boardService.getDetail(num);
		if (board == null) {
			System.out.println("상세보기 실패");
			mv.setViewName("error/error");
			mv.addObject("url", request.getRequestURL());
			mv.addObject("message", "상세보기 실패입니다.");
		} else {
			System.out.println("상세보기 성공");
			int count = commentService.getListCount(num);
			mv.setViewName("board/qna_board_view");
			mv.addObject("count", count);
			mv.addObject("boarddata", board);
		}
		return mv;
	}

	@ResponseBody
	@RequestMapping(value = "/BoardListAjax.bo")
	public Map<String, Object> boardListAjax(
			@RequestParam(value = "page", defaultValue = "1", required = false) int page,
			@RequestParam(value = "limit", defaultValue = "10", required = false) int limit) {

		int listcount = boardService.getListCount();

		int maxpage = (listcount + limit - 1) / limit;

		int startpage = ((page - 1) / 10) * 10 + 1;

		// endpage: 현재 페이지 그룹에서 보여줄 마지막 페이지 수([10],[20],[30] 등..)
		int endpage = startpage + 10 - 1;

		if (endpage > maxpage)
			endpage = maxpage;

		List<Board> boardlist = boardService.getBoardList(page, limit);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("page", page);
		map.put("maxpage", maxpage);
		map.put("startpage", startpage);
		map.put("endpage", endpage);
		map.put("listcount", listcount);
		map.put("boardlist", boardlist);
		map.put("limit", limit);

		return map;
	}

	@GetMapping("/BoardModifyView.bo")
	public ModelAndView BoardModifyView(int num, ModelAndView mv, HttpServletRequest request) {
		Board boarddata = boardService.getDetail(num);
		// 글 내용 불러오기 실패
		if (boarddata == null) {
			System.out.println("(수정)상세보기 실패");
			mv.setViewName("error/error");
			mv.addObject("url", request.getRequestURL());
			mv.addObject("message", "(수정)상세보기 실패입니다.");
			return mv;
		}
		System.out.println("(수정)상세보기 성공");

		// 수정 폼 페이지로 이동할 때 원문 글 내용을 보여주기 때문에 boarddata 객체를
		// modelAndView 객체에 저장
		mv.addObject("boarddata", boarddata);

		// 글 수정 폼 페이지로 이동하기위해 경로를 설정합니다.
		mv.setViewName("board/qna_board_modify");
		return mv;
	}

	@PostMapping("BoardModifyAction.bo")
	public ModelAndView BoardModifyAction(String before_file,Board board, String check, ModelAndView mv, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		boolean usercheck = boardService.isBoardWriter(board.getBOARD_NUM(), board.getBOARD_PASS());
		// 비밀번호가 다른 경우
		if (usercheck == false) {
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('비밀번호가 다릅니다.');");
			out.println("history.back();");
			out.println("</script>");
			out.close();
			return null;
		}
		MultipartFile uploadfile = board.getUploadfile();
		// String saveFolder = request.getSession().getServletContext().getRealPath("resources") + "/upload/";
		if (check != null && check.equals("")) {
			System.out.println("기존 파일을 그대로 사용합니다.");
			board.setBOARD_ORIGINAL(check);
		} else {
			if(uploadfile != null && !uploadfile.isEmpty()) {
				System.out.println("파일 변경되었습니다.");
				String fileName = uploadfile.getOriginalFilename();
				board.setBOARD_ORIGINAL(fileName);
				String fileDBName = fileDBName(fileName, saveFolder);
				
				uploadfile.transferTo(new File(saveFolder+fileDBName));
				board.setBOARD_FILE(fileDBName);
			} else { // 파일 선택하지 않은 경우
				System.out.println("선택 파일이 없습니다.");
				// <input type="hidden" name="BOARD_FILE" value="${boarddata.BOARD_FILE}">
				// 위 태그에 값이 있다면 ""로 값 변경
				board.setBOARD_FILE("");
				board.setBOARD_ORIGINAL("");
			}
		}

		// DAO에서 수정 메소드 호출하여 수정합니다.
		int result = boardService.boardModify(board);

		// 수정에 실패한 경우
		if (result == 0) {
			System.out.println("게시판 수정 실패");
			mv.setViewName("error/error");
			mv.addObject("url", request.getRequestURL());
			mv.addObject("message", "게시판 수정 실패");
		} else {
			System.out.println("게시판 수정 완료");
			
			// 수정전 파일이 있고 새로운 파일을 선택한 경우는 삭제할 목록을 테이블에 추가합니다.
			if(!before_file.equals("") && !before_file.equals(board.getBOARD_FILE())) {
				boardService.insert_deleteFile(before_file);
			}
			String url = "redirect:BoardDetailAction.bo?num=" + board.getBOARD_NUM();
			// 수정한 글 내용을 보여주기 위해 글 내용 보기 보기페이지로 이동하기 위해 경로를 설정합니다.
			mv.setViewName(url);
		}
		return mv;
	}
	
	@PostMapping(value="BoardDeleteAction.bo")
	public ModelAndView BoardDelete(int num, String BOARD_PASS, ModelAndView mv, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		boolean usercheck = boardService.isBoardWriter(num, BOARD_PASS);
		// 비밀번호가 다른 경우
		if (usercheck == false) {
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('비밀번호가 다릅니다.');");
			out.println("history.back();");
			out.println("</script>");
			out.close();
			return null;
		}
		int result = boardService.boardDelete(num);
		if (result == 0) {
			System.out.println("게시판 삭제 실패");
			mv.setViewName("error/error");
			mv.addObject("url", request.getRequestURL());
			mv.addObject("message", "게시판 삭제 실패");
			return mv;
		} else {
			System.out.println("게시판 삭제 완료");
			
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('삭제 되었습니다.');");
			out.println("location.href='BoardList.bo';");
			out.println("</script>");
			out.close();
			return null;
		}
	}
	
	@GetMapping("BoardReplyView.bo")
	public ModelAndView BoardReplyView(int num, ModelAndView mv, HttpServletRequest request) {
		Board board = boardService.getDetail(num);
		if(board==null) {
			mv.setViewName("error/error");
			mv.addObject("url", request.getRequestURL());
			mv.addObject("message", "게시판 답변글 가져오기 실패");
		} else {
			mv.addObject("boarddata", board);
			mv.setViewName("board/qna_board_reply");
		} return mv;
	}
	
	@PostMapping("BoardReplyAction.bo")
	public ModelAndView BoardReplyAction(Board board, ModelAndView mv, HttpServletRequest request) {
		int result = boardService.boardReply(board);
		if(result==0) {
			mv.setViewName("error/error");
			mv.addObject("url", request.getRequestURL());
			mv.addObject("message", "게시판 답변 처리 실패");
		}else {
			System.out.println("게시판 답변 처리 성공");
			String url = "redirect:BoardDetailAction.bo?num=" + board.getBOARD_NUM();
			mv.setViewName(url);
		} return mv;
	}
	
	@GetMapping("BoardFileDown.bo")
	public void BoardFileDown(String filename, String original, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String savePath = "resources/upload";
		
		// 서블릿의 실행 환경 정보를 담고 있는 객체를 리턴
		ServletContext context = request.getSession().getServletContext();
		String sDownloadPath = context.getRealPath(savePath);
		
		String sFilePath = sDownloadPath + "/" +filename;
		System.out.println(sFilePath);
		
		byte b[] =new byte[4096];
		String sMimeType = context.getMimeType(sFilePath);
		System.out.println("sMimeType >>> " + sMimeType);
		
		if (sMimeType == null)
			sMimeType = "application/octet-stram";
		response.setContentType(sMimeType);

		String sEncoding = new String(original.getBytes("UTF-8"), "ISO-8859-1");
		System.out.println(sEncoding);

		response.setHeader("Content-Disposition", "attachment; filename=" + sEncoding);
		
		try (
			// 웹 브라우저의 출력 스트림 생성
			BufferedOutputStream out2 = new BufferedOutputStream(response.getOutputStream());

			// sFilePath로 지정한 파일에 대한 입력 스트림을 생성
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(sFilePath));) {
			int numRead;
			// read(b,0,b.length) : 바이트 배열 b의 0번부터 b.length 크기 만큼 읽어옵니다.
			while ((numRead = in.read(b, 0, b.length)) != -1) { // 읽을 데이터가 존재하는 경우
				// 바이트 배열 b의 0번부터 numRead크기 만큼 브라우저로 출력
				out2.write(b, 0, numRead);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String fileDBName(String fileName, String saveFolder) {
		// 새로운 폴더 이름 : 오늘 년-월-일
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int date = c.get(Calendar.DATE);

		String homedir = saveFolder + year + "-" + month + "-" + date;
		System.out.println(homedir);
		File path1 = new File(homedir);
		if (!(path1.exists())) {
			path1.mkdir(); // 새로운 폴더 생성
		}

		// 난수 발생
		Random r = new Random();
		int random = r.nextInt(100000000);

		/** 확장자 구하기 시작 **/
		int index = fileName.lastIndexOf(".");
		System.out.println("index = " + index);
		String fileExtension = fileName.substring(index + 1);
		System.out.println("fileExtension = " + fileExtension);
		/** 확장자 구하기 끝 **/

		// 새로운 파일명
		String refileName = "bbs" + year + month + date + random + "." + fileExtension;
		System.out.println("refileName = " + refileName);

		// 오라클 db에 저장될 파일명
		String fileDBName = "/" + year + "-" + month + "-" + date + "/" + refileName;
		System.out.println("fileDBName = " + fileDBName);

		return fileDBName;
	}
}
