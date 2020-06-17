<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MVC 게시판</title>
<jsp:include page="header.jsp"/>
<script src="resources/js/reply.js"></script>
<style>
h1{font-size:1.5rem;text-align: center;color:#1a92b9}
label{font-weight: bold}
.container{width:60%}
form > div:last-child {text-align: right;}
</style>
</head>
<body>
<div class="container">
	<form action="BoardReplyAction.bo" method="post" name="boardform">
		<input type="hidden" name="BOARD_NUM" value="${boarddata.BOARD_NUM }">
		<input type="hidden" name="BOARD_RE_REF" value="${boarddata.BOARD_RE_REF }">
		<input type="hidden" name="BOARD_RE_LEV" value="${boarddata.BOARD_RE_LEV }">
		<input type="hidden" name="BOARD_RE_SEQ" value="${boarddata.BOARD_RE_SEQ }">
		<h1>MVC 게시판 - 답글</h1>
		<div class="form-group">
			<label for="board_name">글쓴이</label>
			<input name="BOARD_NAME" id="board_name" type="text" value="${id }"
				class="form-control" readOnly>
		</div>
		<div class="form-group">
			<label for="board_subject">제목</label>
			<input name="BOARD_SUBJECT" id="board_subject" type="text" size="50"
				class="form-control" maxlength="100" value="Re:${boarddata.BOARD_SUBJECT }">
		</div>
		<div class="form-group">
			<label for="board_content">내용</label>
			<textarea name="BOARD_CONTENT" id="board_content" cols="67" rows="15"
			 class="form-control"></textarea>
		</div>
		<div class="form-group">
			<label for="board_pass">비밀번호</label>
			<input name="BOARD_PASS" id="board_pass" type="password" size="10"
			 maxlength="30" class="form-control" placeholder="Enter board_password" required>
		</div>
		<div class="form-group">
			<input type="submit" class="btn btn-primary" value="등록">
			<input type="button" class="btn btn-danger" onClick="history.go(-1)" value="취소">
		</div>
	</form>
</div>
</body>
</html>