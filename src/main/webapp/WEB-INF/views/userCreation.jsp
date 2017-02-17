<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="<c:url value="/resources/theme/styles.css" />" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User Creation</title>
</head>
<body>
<div id="topBarContainer" style="height:100px; width:1000px; border:1px solid; margin:auto; background-color:cyan;">
<h1 style="margin:auto auto auto auto; float:left;">Chess Web</h1>
	<div id="menuContainer" style="height:50px; margin: 50px auto auto auto; background-color:grey;">
		<ul id="mainMenuBar">
			<li class="mainMenuItem" onclick="location.href='home'">
				<p onclick="location.href='home'">Home</p>
			</li>
		</ul>
	</div>
</div>

<div id="mainContainer">
	<h1 id="title">User Account Creation</h1>


	<form:form method="post" action="createUser" modelAttribute="user">
	    <table id="userCreationTable">
		    <tbody>
			    <tr>
			        <td><form:label path="username">Username:</form:label></td>
			        <td><form:input path="username"></form:input> </td> 
			        <td><p id="errorMessage"> <form:errors path="username"></form:errors> </p></td>
			    </tr>
			     
			    <tr>    
			        <!--  <td><form:label path="email">Email:</form:label></td> -->
			        <td><form:input type="hidden" path="email" value="none"></form:input></td> 
			        <td><p id="errorMessage"> <form:errors path="email"></form:errors> </p></td>
			    </tr>
			    
			    <tr>    
			        <td><form:label path="password">Password:</form:label></td>
			        <td><form:password path="password"></form:password></td>
			        <td><p id="errorMessage"> <form:errors path="password"></form:errors> </p></td>
			    </tr>
			    <tr>    
			        <td><form:label path="passwordConfirmation">Confirm Password:</form:label></td>
			        <td><form:password path="passwordConfirmation"></form:password></td>
			        <td><p id="errorMessage"> <form:errors path="passwordConfirmation"></form:errors> </p></td>
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

</body>
</html>