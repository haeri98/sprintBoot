<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 정보</title>
<style>
	table{margin: 0 auto; width:50%; border: 1px solid #ddd}
	*{text-align: center;}
	tr td{height:40px;}
	tr>td:nth-child(1){width:30%}
	tr>td:nth-child(2) {width:50%}
</style>
</head>
<body>
<jsp:include page="../board/header.jsp" />
<c:set var="m" value="${memberinfo }"/>
<table class="table table-striped">
	<tr><td>아이디</td><td>${m.id }</td></tr>
	<tr><td>비밀번호</td><td>${m.password }</td></tr>
	<tr><td>이름</td><td>${m.name }</td></tr>
	<tr><td>나이</td><td>${m.age }</td></tr>
	<tr><td>성별</td><td>${m.gender }</td></tr>
	<tr><td>이메일</td><td>${m.email }</td></tr>
	<tr><td colspan="2"><a href="member_list.net">리스트로 돌아가기</a></td></tr>
</table>
</body>
</html>