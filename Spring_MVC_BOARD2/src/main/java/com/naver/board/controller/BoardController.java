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
	
	// �Ӽ�=�� �������� �ۼ�
	// savefoldername=D:\\springWork\\Spring_MVC_BOARD2\\src\\main\webapp\\resources\\upload\\
	// ���� �������� ���ؼ��� �Ӽ��� �̿��մϴ�.
	@Value("${savefoldername}")
	private String saveFolder;
	
	// �۾���
	// @RequestMapping(value = "/BoardWrite.bo", method = RequestMethod.GET)
	@GetMapping(value = "/BoardWrite.bo")
	public String board_write() {
		return "board/qna_board_write";
	}

	// �Խ��� ����
	// @RequestMapping(value = "/board_write_ok.bo", method = RequestMethod.POST)
	@PostMapping(value = "/Board_write_ok.bo")
	public String board_write_ok(Board board) {
		boardService.insertBoard(board);
		return "redirect:/BoardList.bo";
	}

	/*
	 * ������ �����̳ʴ� �Ű����� BbsBean��ü�� �����ϰ� BbsBeans��ü�� setter �޼��带 ȣ���Ͽ� ����� �Է°��� �����մϴ�.
	 * �Ű������� �̸��� setter�� property�� ��ġ�ϸ� �˴ϴ�.
	 */
	@PostMapping(value = "/BoardAddAction.bo")
	// @RequestMapping(value = "/BoardAddAction.bo", method = Requesethod.POST)
	public String bbs_wrtie_ok(Board board, HttpServletRequest request) throws Exception {
		MultipartFile uploadfile = board.getUploadfile();
		if (!uploadfile.isEmpty()) {
			String fileName = uploadfile.getOriginalFilename(); // ���� ���ϸ�
			board.setBOARD_ORIGINAL(fileName); // ���� ���ϸ� ����

			// ���ο� ���� �̸� : ���� ��-��-��
			Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			int date = c.get(Calendar.DATE);
			//String saveFolder = request.getSession().getServletContext().getRealPath("resources") + "/upload/";
			String homedir = saveFolder + year + "-" + month + "-" + date;
			System.out.println(homedir);
			File path1 = new File(homedir);
			if (!(path1.exists())) {
				path1.mkdir(); // ���ο� ���� ����
			}

			// ���� �߻�
			Random r = new Random();
			int random = r.nextInt(100000000);

			/** Ȯ���� ���ϱ� ���� **/
			int index = fileName.lastIndexOf(".");
			/*
			 * ���ڿ����� Ư�����ڿ��� ��ġ ��(index)�� ��ȯ�Ѵ�. indexOf�� ó�� �߰ߵǴ� ���ڿ��� ���� index�� ��ȯ�ϴ� �ݸ�,
			 * lastIndexOf�� ���������� �߰ߵǴ� ���ڿ��� index�� ��ȯ�մϴ�. (���ϸ� ���� ������ ���� ��� �� �������� �߰ߵǴ�
			 * ���ڿ��� ��ġ�� �����մϴ�.)
			 */
			System.out.println("index = " + index);

			String fileExtension = fileName.substring(index + 1);
			System.out.println("fileExtension = " + fileExtension);
			/** Ȯ���� ���ϱ� �� **/

			// ���ο� ���ϸ�
			String refileName = "bbs" + year + month + date + random + "." + fileExtension;
			System.out.println("refileName = " + refileName);

			// ����Ŭ db�� ����� ���ϸ�
			String fileDBName = "/" + year + "-" + month + "-" + date + "/" + refileName;
			System.out.println("fileDBName = " + fileDBName);

			// ���ε��� ������ �Ű������� ��ο� ����
			uploadfile.transferTo(new File(saveFolder + fileDBName));

			// �ٲ� ���ϸ����� ����
			board.setBOARD_FILE(fileDBName);
		}
		boardService.insertBoard(board); // ���� �޼��� ȣ��
		return "redirect:BoardList.bo";
	}

	@RequestMapping(value = "/BoardList.bo")
	public ModelAndView boardList(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
			ModelAndView mv) {

		int limit = 10;

		// �� ����Ʈ ���� �޾ƿɴϴ�.
		int listcount = boardService.getListCount();

		int maxpage = (listcount + limit - 1) / limit;

		int startpage = ((page - 1) / 10) * 10 + 1;

		// endpage: ���� ������ �׷쿡�� ������ ������ ������ ��([10],[20],[30] ��..)
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
			System.out.println("�󼼺��� ����");
			mv.setViewName("error/error");
			mv.addObject("url", request.getRequestURL());
			mv.addObject("message", "�󼼺��� �����Դϴ�.");
		} else {
			System.out.println("�󼼺��� ����");
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

		// endpage: ���� ������ �׷쿡�� ������ ������ ������ ��([10],[20],[30] ��..)
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
		// �� ���� �ҷ����� ����
		if (boarddata == null) {
			System.out.println("(����)�󼼺��� ����");
			mv.setViewName("error/error");
			mv.addObject("url", request.getRequestURL());
			mv.addObject("message", "(����)�󼼺��� �����Դϴ�.");
			return mv;
		}
		System.out.println("(����)�󼼺��� ����");

		// ���� �� �������� �̵��� �� ���� �� ������ �����ֱ� ������ boarddata ��ü��
		// modelAndView ��ü�� ����
		mv.addObject("boarddata", boarddata);

		// �� ���� �� �������� �̵��ϱ����� ��θ� �����մϴ�.
		mv.setViewName("board/qna_board_modify");
		return mv;
	}

	@PostMapping("BoardModifyAction.bo")
	public ModelAndView BoardModifyAction(String before_file,Board board, String check, ModelAndView mv, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		boolean usercheck = boardService.isBoardWriter(board.getBOARD_NUM(), board.getBOARD_PASS());
		// ��й�ȣ�� �ٸ� ���
		if (usercheck == false) {
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('��й�ȣ�� �ٸ��ϴ�.');");
			out.println("history.back();");
			out.println("</script>");
			out.close();
			return null;
		}
		MultipartFile uploadfile = board.getUploadfile();
		// String saveFolder = request.getSession().getServletContext().getRealPath("resources") + "/upload/";
		if (check != null && check.equals("")) {
			System.out.println("���� ������ �״�� ����մϴ�.");
			board.setBOARD_ORIGINAL(check);
		} else {
			if(uploadfile != null && !uploadfile.isEmpty()) {
				System.out.println("���� ����Ǿ����ϴ�.");
				String fileName = uploadfile.getOriginalFilename();
				board.setBOARD_ORIGINAL(fileName);
				String fileDBName = fileDBName(fileName, saveFolder);
				
				uploadfile.transferTo(new File(saveFolder+fileDBName));
				board.setBOARD_FILE(fileDBName);
			} else { // ���� �������� ���� ���
				System.out.println("���� ������ �����ϴ�.");
				// <input type="hidden" name="BOARD_FILE" value="${boarddata.BOARD_FILE}">
				// �� �±׿� ���� �ִٸ� ""�� �� ����
				board.setBOARD_FILE("");
				board.setBOARD_ORIGINAL("");
			}
		}

		// DAO���� ���� �޼ҵ� ȣ���Ͽ� �����մϴ�.
		int result = boardService.boardModify(board);

		// ������ ������ ���
		if (result == 0) {
			System.out.println("�Խ��� ���� ����");
			mv.setViewName("error/error");
			mv.addObject("url", request.getRequestURL());
			mv.addObject("message", "�Խ��� ���� ����");
		} else {
			System.out.println("�Խ��� ���� �Ϸ�");
			
			// ������ ������ �ְ� ���ο� ������ ������ ���� ������ ����� ���̺� �߰��մϴ�.
			if(!before_file.equals("") && !before_file.equals(board.getBOARD_FILE())) {
				boardService.insert_deleteFile(before_file);
			}
			String url = "redirect:BoardDetailAction.bo?num=" + board.getBOARD_NUM();
			// ������ �� ������ �����ֱ� ���� �� ���� ���� ������������ �̵��ϱ� ���� ��θ� �����մϴ�.
			mv.setViewName(url);
		}
		return mv;
	}
	
	@PostMapping(value="BoardDeleteAction.bo")
	public ModelAndView BoardDelete(int num, String BOARD_PASS, ModelAndView mv, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		boolean usercheck = boardService.isBoardWriter(num, BOARD_PASS);
		// ��й�ȣ�� �ٸ� ���
		if (usercheck == false) {
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('��й�ȣ�� �ٸ��ϴ�.');");
			out.println("history.back();");
			out.println("</script>");
			out.close();
			return null;
		}
		int result = boardService.boardDelete(num);
		if (result == 0) {
			System.out.println("�Խ��� ���� ����");
			mv.setViewName("error/error");
			mv.addObject("url", request.getRequestURL());
			mv.addObject("message", "�Խ��� ���� ����");
			return mv;
		} else {
			System.out.println("�Խ��� ���� �Ϸ�");
			
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('���� �Ǿ����ϴ�.');");
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
			mv.addObject("message", "�Խ��� �亯�� �������� ����");
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
			mv.addObject("message", "�Խ��� �亯 ó�� ����");
		}else {
			System.out.println("�Խ��� �亯 ó�� ����");
			String url = "redirect:BoardDetailAction.bo?num=" + board.getBOARD_NUM();
			mv.setViewName(url);
		} return mv;
	}
	
	@GetMapping("BoardFileDown.bo")
	public void BoardFileDown(String filename, String original, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String savePath = "resources/upload";
		
		// ������ ���� ȯ�� ������ ��� �ִ� ��ü�� ����
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
			// �� �������� ��� ��Ʈ�� ����
			BufferedOutputStream out2 = new BufferedOutputStream(response.getOutputStream());

			// sFilePath�� ������ ���Ͽ� ���� �Է� ��Ʈ���� ����
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(sFilePath));) {
			int numRead;
			// read(b,0,b.length) : ����Ʈ �迭 b�� 0������ b.length ũ�� ��ŭ �о�ɴϴ�.
			while ((numRead = in.read(b, 0, b.length)) != -1) { // ���� �����Ͱ� �����ϴ� ���
				// ����Ʈ �迭 b�� 0������ numReadũ�� ��ŭ �������� ���
				out2.write(b, 0, numRead);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String fileDBName(String fileName, String saveFolder) {
		// ���ο� ���� �̸� : ���� ��-��-��
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int date = c.get(Calendar.DATE);

		String homedir = saveFolder + year + "-" + month + "-" + date;
		System.out.println(homedir);
		File path1 = new File(homedir);
		if (!(path1.exists())) {
			path1.mkdir(); // ���ο� ���� ����
		}

		// ���� �߻�
		Random r = new Random();
		int random = r.nextInt(100000000);

		/** Ȯ���� ���ϱ� ���� **/
		int index = fileName.lastIndexOf(".");
		System.out.println("index = " + index);
		String fileExtension = fileName.substring(index + 1);
		System.out.println("fileExtension = " + fileExtension);
		/** Ȯ���� ���ϱ� �� **/

		// ���ο� ���ϸ�
		String refileName = "bbs" + year + month + date + random + "." + fileExtension;
		System.out.println("refileName = " + refileName);

		// ����Ŭ db�� ����� ���ϸ�
		String fileDBName = "/" + year + "-" + month + "-" + date + "/" + refileName;
		System.out.println("fileDBName = " + fileDBName);

		return fileDBName;
	}
}
