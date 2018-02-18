<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="<c:url value="/resources/theme/styles.css" />" rel="stylesheet">
<script type="text/javascript" src="<c:url value="/resources/javascript/admin.js" />"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Admin Panel</title>

<script>connect();</script>

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
	Admin Panel 
</h1>

<div id="adminCreationBox">
	<p id="adminCreationSuccess">${creationSuccess}</p>
	<form:form method="post" action="createAdmin">
	    <table id="userCreationTable">
		    <tbody>
			    <tr>
			        <td><form:label path="username">Username:</form:label></td>
			        <td><form:input path="username"></form:input> </td> 
			        <td><p id="errorMessage"> ${errorMessageUser} </p></td>
			    </tr>
			    <tr>    
			        <td><form:label path="email">Email:</form:label></td>
			        <td><form:input path="email"></form:input></td> 
			        <td><p id="errorMessage"> ${errorMessageEmail} </p></td>
			    </tr>
			    <tr>    
			        <td><form:label path="password">Password:</form:label></td>
			        <td><form:password path="password"></form:password></td>
			        <td><p id="errorMessage"> ${errorMessagePassword} </p></td>
			    </tr>
			    <tr>    
			        <td><form:label path="confirmPassword">Confirm Password:</form:label></td>
			        <td><form:password path="confirmPassword"></form:password></td>
			    </tr>
			    <tr>    
			        <td>
			        <input id="loginButton" type="submit" value="Create">
			        </td>
			    </tr>
			</tbody>
		</table>      
	</form:form>

</div>

<div id="liveStatsBox">
	<p style="text-align:center; font-weight:bold; margin:auto;" id="liveStatsP">Live Stats</p>
	<p id="players">---</p>
	<p id="games">---</p>
</div>

</div>


</body>
</html>