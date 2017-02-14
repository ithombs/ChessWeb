<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<h1 style="margin:auto auto auto auto; float:left;">Chess Web</h1>
<sec:authentication var="user" property="principal" />
<!-- This is the basic login form. If a user is logged in then it will present a logout option, otherwise the login form will be displayed. -->
<c:choose>
	<c:when test="${user.equals('anonymousUser')}">
		<form method="post" action="login">
		    <table id="loginTable">
			    <tbody>
			    <tr>
			        <td><label >Username</label></td>
			        <td><input name="username"></input></td> 
			        <td><label >Password</label></td>
			        <td><input id="password" name="password" type="password"></input></td>
			        <td><input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" /></td>
			        <td><input id="loginButton" type="submit" value="Login"></td>
			    </tr>
			    <tr>
			    	<td>
			    		<c:if test="${param.error != null}">
			            	<p>Invalid username and password.</p>
			            </c:if>
			    	</td>
			    </tr>
				</tbody>
			</table>    
		     
		</form>
	</c:when>
	<c:otherwise>
		<table id="logoutTable">
			<tbody>
				<tr>
					<td>
						Welcome ${user.username}! - <a href=logout>Log out</a>
					</td>
				</tr>
				<tr>
					<td>
						<c:choose>
							<c:when test="${roles != null && roles.contains('admin')}">
								<a href=admin>Admin Panel</a>
							</c:when>
						</c:choose>
					</td>
				</tr>
			</tbody>
		</table>
	</c:otherwise>
</c:choose>