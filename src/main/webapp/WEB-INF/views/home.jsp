<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page session="false" %>
<html>
<head>
<link href="<c:url value="/resources/theme/styles.css" />" rel="stylesheet">
	<title>Home</title>
	
	
<script type="text/javascript"
    src="http://code.jquery.com/jquery-1.10.1.min.js"></script>
	<script type="text/javascript">
    function ajaxTest() {
        $.ajax({
            url : 'ajaxtest.html',
            success : function(data) {
                $('#result').html(data);
            }
        });
    }
</script>


<script type="text/javascript">
    //var intervalId = 0;
    //intervalId = setInterval(ajaxTest, 1000);
</script>

</head>
<body>

<div id="topBarContainer" style="height:100px; width:1000px; border:1px solid; margin:auto;">
	<%@ include file="loginForm.jsp" %>
	
	<div id="menuContainer" style="height:50px; margin: 50px auto auto auto;">
		<%@ include file="topNavBar.jsp" %>
	</div>
</div>


<div id="mainContainer" style="width:1000px; border:1px solid; margin: 50px auto;">


<h1 id="title">
	Welcome to Chess Web! 
</h1>

<p style="text-align:center;"><img id="landingPageImg" src="<c:url value="/resources/images/chessBoard.jpg" />"></p>

<p class="center" style="margin-bottom:0px; font-size:20px">Leaderboard</p>
<table id="leaderboard">
	
	<tr>
		<th>Rank</th><th>Username</th><th>Wins</th><th>Win Ratio</th>
	</tr>
	<c:forEach var="user" items="${leaderboard}" varStatus="loopCounter">
	<tr>
		<td><c:out value="${loopCounter.index + 1}"/></td>
		<td><c:out value='${user.username}'/></td>
		<td><c:out value='${user.wins}'/></td>
		<td><c:out value="${user.winPercentage}%"/></td>
	</tr>
	</c:forEach>
</table>

<!-- <p style="font-size:75px">&#9812</p> -->

</div>
</body>
</html>
