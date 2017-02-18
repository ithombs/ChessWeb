<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="<c:url value="/resources/theme/styles.css" />" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Profile</title>
</head>
<body>

<div id="topBarContainer" style="height:100px; width:1000px; border:1px solid; margin:auto; background-color:cyan;">
	
	<%@ include file="loginForm.jsp" %>
	
	<div id="menuContainer" style="height:50px; margin: 50px auto auto auto; background-color:grey;">
		<%@ include file="topNavBar.jsp" %>
	</div>
</div>

<div id="mainContainer" style="width:1000px; border:1px solid; margin: 50px auto;">
<h1 id="title">
	${username}'s Profile 
</h1>

<div id="statsBox1">
<h3 style="text-align:center;">Chess Stats</h3>
<table id="statsTable" style="margin:0 auto;">
	<tr><th>Black Side Wins</th></tr>
	<tr><td>${blackWins}</td></tr>
	<tr><th>White Side Wins</th></tr>
	<tr><td>${whiteWins}</td></tr>
	<tr><th>Total Wins</th></tr>
	<tr><td>${totalWins}</td></tr>
	<tr><th>Total Games Played</th></tr>
	<tr><td>${totalGames}</td></tr>
	<tr><th>Total Win Ratio</th></tr>
	<tr><td><fmt:formatNumber value="${totalWins/totalGames}" type="percent"/></td></tr>
</table>
<!-- 
	<p id="stats1">
	Black Side Wins: ${blackWins}
	<br/> 
	White Side Wins: ${whiteWins}
	<br/>
	Total Wins: ${totalWins}
	<br/>
	Total Games Played: ${totalGames}
	<br/>
	Total Win Ratio: <fmt:formatNumber value="${totalWins/totalGames}" type="percent"/>
	</p>
 -->
</div>

<div id="accountOptions">
	<h3 style="text-align:center;">Password Change</h3>
	<br/>
	<p style="text-align:center; color:red;">${passChangeResult}</p>
	<br/>
	<form action="/test1/passwordChange.html" method="POST">
		Old Password: <input type="password" name="oldPass">
		<br/>
		New Password:<input type="password" name="newPass">
		<br/>
		Confirm Password:<input type="password" name="confirmNewPass">
		<br/>
		<button type="submit" id="passChangeBtn">Submit</button>
	</form>
</div>

</div><!-- End of main container -->

</body>
</html>