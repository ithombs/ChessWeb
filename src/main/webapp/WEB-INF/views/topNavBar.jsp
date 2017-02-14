<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<ul id="mainMenuBar">
	<li class="mainMenuItem" onclick="location.href='home'">
		<p onclick="location.href='home'">Home</p>
	</li>
	<li class="mainMenuItem" onclick="location.href='playChess'">
		<p onclick="location.href='playChess'">Play</p>
	</li>
	
	<!-- Display certain menu options to logged in users only and user creation to non-logged in users -->
	<c:choose>
		<c:when test="${!user.equals('anonymousUser')}">
			<li class="mainMenuItem" onclick="location.href='chessReplays'">
				<p onclick="location.href='chessReplays'">Chess Replays</p>
			</li>
			<li	class="mainMenuItem" onclick="location.href='profile'">
				<p onclick="location.href='profile'">Profile</p>
			</li>
		</c:when>
		
		<c:otherwise>
			<li class="mainMenuItem" onclick="location.href='createUser'">
				<p onclick="location.href='createUser'">Create Account</p>
			</li>
		</c:otherwise>
	</c:choose>
	
</ul>

