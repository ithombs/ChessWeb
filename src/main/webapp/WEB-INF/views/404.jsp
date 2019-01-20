<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>
		<link href="<c:url value="/resources/theme/styles.css" />" rel="stylesheet">
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Invalid Move - Page Not Found!</title>
	</head>
	<body>
		<div id="topBarContainer" style="height:100px; width:1000px; border:1px solid; margin:auto; background-color:cyan;">
			<%@ include file="loginForm.jsp" %>
			
			<div id="menuContainer" style="height:50px; margin: 50px auto auto auto; background-color:grey;">
				<%@ include file="topNavBar.jsp" %>
			</div>
		</div>
		
		<div id="mainContainer" style="width:1000px; border:1px solid; margin: 50px auto;">
			<div style="font-size:40px; font-weight: bold; text-align:center;">
				Invalid Move!
			</div>
		</div>
	</body>
</html>