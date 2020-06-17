<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
  <script src="resources/js/jquery-3.5.0.js"></script>
<style>
body > nav.navbar {justify-content: flex-end;/*오른쪽 정렬*/}
.dropdown-menu{min-width: 0rem;} 
/*nav 색상 지정*/
.navbar{background: #096988;margin-bottom: 3em;padding-right: 3em;}
.navbar-dark .navbar-nav .nav-link{color: rgb(255,255,255);}
</style>
</head>
<body>
<c:if test="${empty id }">
	<script>location.href="login.net"</script>
</c:if>
<nav class="navbar navbar-expand-sm right-block navbar-dark">
	<ul class="navbar-nav">
		<c:if test="${!empty id }">
			<li class="nav-item">
				<a class="nav-link" href="logout.net">${id }님 (로그아웃)</a>
			</li>
			<li class="nav-item"><a class="nav-link" href="member_update.net">정보수정</a></li>
			<c:if test="${id=='admin' }">
				<li class="nav-item dropdown">
					<a class="nav-link dropdown-toggle" href="#" id="navbardrop"
					 data-toggle="dropdown">관리자</a>
					<div class="dropdown-menu">
						<a class="dropdown-item" href="member_list.net">회원정보</a>
						<a class="dropdown-item" href="BoardList.bo">게시판</a>
					</div></li>
			</c:if>
		</c:if>
	</ul>
</nav>
</body>
</html>