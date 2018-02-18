<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<link href="<c:url value="/resources/theme/styles.css" />" rel="stylesheet">
<script type="text/javascript" src="<c:url value="/resources/javascript/chess.js" />"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.1/sockjs.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Basic Chess</title>
</head>
<body>
<script type="text/javascript"> 
//Connect to the websocket server
	connect();
</script>

<div id="topBarContainer" style="height:100px; width:1000px; border:1px solid; margin:auto; background-color:cyan;">
	
	<%@ include file="loginForm.jsp" %>
	
	<div id="menuContainer" style="height:50px; margin: 50px auto auto auto; background-color:grey;">
		<%@ include file="topNavBar.jsp" %>
	</div>
</div>

<div id="mainContainer" style="width:1000px; border:1px solid; margin: 50px auto;">
<a href="#" style="float:right;" id="mute" onclick=mute();>Mute</a>
<h1 id="title">
	Play Chess! 
</h1>


<h2 class="center" id="side">
</h2>
<p id="timer"> </p>



<div id="gameButtons">
	<h3 style="margin:0 auto;">Game Options</h3>
	<select id="oppSelect" onChange=opponentSelect(this.value)>
		<option value="vsPlayer">Human Opponent</option>
		<option value="vsComp">Computer Opponent</option>
	</select>
	
	<select id="compLevels">
		<option value="0">Level 0</option>
		<option value="1">Level 1</option>
		<option value="2">Level 2</option>
		<option value="3">Level 3</option>
	</select>

	<button type="button" id="queueBtn" onClick="enterQueue('human', 0)">Enter Queue</button>
	<br/>	
	
	<button type="button" id="concedeBtn" onClick=concede()>Concede</button>
	<button type="button" id="playAI" onClick="enterQueue('AI',compLevels.value)">Play</button>
</div>




<!-- Move list goes here -->
<div id="moveListContainer">
	<p id="moveListTitle" style="text-align:center; margin:0;">Move List</p>
	<p id="moveList"></p>
</div>

<!-- This is the basic game board (plain as well, all tiles are white and its a basic grid) -->
<div id="board" style="height:500px; width:500px;  position:relative; margin:auto auto 50px auto;" >
	<div style="height:500px; width:12.3%; position:absolute; ">
		<div id="0|0" class="darkTile" title="a8" style="height:12.1%; width:100%;border-left:1px solid;border-top:1px solid;"   ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="1|0" title="a7" style="height:12.1%; width:100%;border-left:1px solid;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="2|0" class="darkTile" title="a6" style="height:12.1%; width:100%;border-left:1px solid;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="3|0" title="a5" style="height:12.1%; width:100%;border-left:1px solid;"   ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="4|0" class="darkTile" title="a4" style="height:12.1%; width:100%;border-left:1px solid;"   ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="5|0" title="a3" style="height:12.1%; width:100%;border-left:1px solid;"   ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="6|0" class="darkTile" title="a2" style="height:12.1%; width:100%;border-left:1px solid;"   ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="7|0" title="a1" style="height:12.1%; width:100%;border-left:1px solid; border-bottom:1px solid;"   ondrop="drop(event)" ondragover="allowDrop(event)"></div>
	</div>
	
	<div style="height:500px; width:12.3%; position:absolute; left:12.3%">
		<div id="0|1" title="b8" style="height:12.1%; width:100%;border-top:1px solid;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="1|1" class="darkTile" title="b7" style="height:12.1%; width:100%;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="2|1" title="b6" style="height:12.1%; width:100%;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="3|1" class="darkTile" title="b5" style="height:12.1%;" width:100%;  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="4|1" title="b4" style="height:12.1%; width:100%;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="5|1" class="darkTile" title="b3" style="height:12.1%;" width:100%;  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="6|1" title="b2" style="height:12.1%; width:100%;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="7|1" class="darkTile" title="b1" style="height:12.1%; width:100%; border-bottom:1px solid;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
	</div>
	
	<div style="height:500px; width:12.3%; position:absolute; left:24.6%">
		<div id="0|2" class="darkTile" title="c8" style="height:12.1%; width:100%;border-top:1px solid;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="1|2" title="c7" style="height:12.1%; width:100%;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="2|2" class="darkTile" title="c6" style="height:12.1%; width:100%; " ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="3|2" title="c5" style="height:12.1%; width:100%;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="4|2" class="darkTile" title="c4" style="height:12.1%; width:100%;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="5|2" title="c3" style="height:12.1%; width:100%;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="6|2" class="darkTile" title="c2" style="height:12.1%; width:100%;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="7|2" title="c1" style="height:12.1%; width:100%; border-bottom:1px solid;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
	</div>
	
	<div style="height:500px; width:12.3%; position:absolute; left:36.9%">
		<div id="0|3" title="d8" style="height:12.1%; width:100%;border-top:1px solid;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="1|3" class="darkTile" title="d7" style="height:12.1%; width:100%;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="2|3" title="d6" style="height:12.1%; width:100%;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="3|3" class="darkTile" title="d5" style="height:12.1%; width:100%;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="4|3" title="d4" style="height:12.1%; width:100%;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="5|3" class="darkTile" title="d3" style="height:12.1%; width:100%;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="6|3" title="d2" style="height:12.1%; width:100%;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="7|3" class="darkTile" title="d1" style="height:12.1%; width:100%; border-bottom:1px solid;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
	</div>
	
	<div style="height:500px; width:12.3%; position:absolute; left:49.2%">
		<div id="0|4" class="darkTile" title="e8" style="height:12.1%; width:100%;border-top:1px solid;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="1|4" title="e7" style="height:12.1%; width:100%;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="2|4" class="darkTile" title="e6" style="height:12.1%; width:100%;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="3|4" title="e5" style="height:12.1%; width:100%;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="4|4" class="darkTile" title="e4" style="height:12.1%; width:100%;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="5|4" title="e3" style="height:12.1%; width:100%;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="6|4" class="darkTile" title="e2" style="height:12.1%; width:100%;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="7|4" title="e1" style="height:12.1%; width:100%; border-bottom:1px solid;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
	</div>
	
	<div style="height:500px; width:12.3%; position:absolute; left:61.5%">
		<div id="0|5" title="f8" style="height:12.1%; width:100%;border-top:1px solid;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="1|5" class="darkTile" title="f7" style="height:12.1%; width:100%;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="2|5" title="f6" style="height:12.1%; width:100%;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="3|5" class="darkTile" title="f5" style="height:12.1%; width:100%;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="4|5" title="f4" style="height:12.1%; width:100%;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="5|5" class="darkTile" title="f3" style="height:12.1%; width:100%;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="6|5" title="f2" style="height:12.1%; width:100%;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="7|5" class="darkTile" title="f1" style="height:12.1%; width:100%; border-bottom:1px solid;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
	</div>
	
	<div style="height:500px; width:12.3%; position:absolute; left:73.8%">
		<div id="0|6" class="darkTile" title="g8" style="height:12.1%; width:100%;border-top:1px solid;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="1|6" title="g7" style="height:12.1%; width:100%;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="2|6" class="darkTile" title="g6" style="height:12.1%; width:100%;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="3|6" title="g5" style="height:12.1%; width:100%;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="4|6" class="darkTile" title="g4" style="height:12.1%; width:100%;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="5|6" title="g3" style="height:12.1%; width:100%;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="6|6" class="darkTile" title="g2" style="height:12.1%; width:100%;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="7|6" title="g1" style="height:12.1%; width:100%;border-bottom:1px solid;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
	</div>
	
	<div style="height:500px; width:12.3%; position:absolute; left:86.1%">
		<div id="0|7" title="h8" style="height:12.1%; width:100%;border-top:1px solid;border-right:1px solid;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="1|7" class="darkTile" title="h7" style="height:12.1%; width:100%;border-right:1px solid;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="2|7" title="h6" style="height:12.1%; width:100%;border-right:1px solid;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="3|7" class="darkTile" title="h5" style="height:12.1%; width:100%;border-right:1px solid;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="4|7" title="h4" style="height:12.1%; width:100%;border-right:1px solid;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="5|7" class="darkTile" title="h3" style="height:12.1%; width:100%;border-right:1px solid;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="6|7" title="h2" style="height:12.1%; width:100%;border-right:1px solid;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
		<div id="7|7" class="darkTile" title="h1" style="height:12.1%; width:100%;border-bottom:1px solid;border-right:1px solid;"  ondrop="drop(event)" ondragover="allowDrop(event)"></div>
	</div>
</div>



</div>


</body>
</html>