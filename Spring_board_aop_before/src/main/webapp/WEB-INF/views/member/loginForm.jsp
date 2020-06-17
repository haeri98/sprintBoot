<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
<link href="resources/css/login.css" rel="stylesheet" type="text/css">
<script src="resources/js/jquery-3.5.0.js"></script>
<script>
	$(function(){
		$(".join").click(function(){
			location.href="join.net";
		});
	})
</script>
</head>

<body>
   <form name="loginform" action="loginProcess.net" method="post">
   	  <h1>로그인</h1><hr>
      <b>ID</b> 
      <input type="text" name="id" id="id" placeholder="Enter id" required
      	<c:if test="${!empty saveid }"> value="${saveid }"</c:if>> 
      <b>Password</b> 
      <input type="password" name="password" id="password" placeholder="Enter password" required>
		<input type="checkbox" name="remember" id="remember" value="store"
			<c:if test="${!empty saveid }"> checked</c:if>>Remember me
      <div class="clearfix">
         <button type="submit" class="submitbtn submit">LOGIN</button>
         <button type="button" class="cancelbtn join">JOIN</button>
      </div>
   </form>
</body>
</html>