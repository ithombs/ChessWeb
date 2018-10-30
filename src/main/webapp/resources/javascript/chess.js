/**
 * This file contains the functions for the WebSocket pertaining to the ChessBoard java object for the client end.
 */
var socket;
var moveFrom;
var moveTo;
var tileFrom;
var tileTo;
var test;
var selfMove;
var cappedPiece;
var audio = new Audio('resources/soundEffects/PieceMove.mp3');
var timer = 0;
var timerVar;
var userN;
var reconn = false;
var replayMoveList;
var mlIndex;
var prevPositions;
var side;
var color = true;
var isMuted = false;

var stompClient;
function initStompChannels(){
	var socket = new SockJS('/ChessWeb/chessEndpoint');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame){
		stompClient.subscribe('/topic/msg', function (msg) {
	        alert(msg);
	    });
		
		stompClient.subscribe('/user/queue/chessMsg', function (msg) {
	        msg = JSON.parse(msg.body);
	        if(msg.chessCommand == "gameStart"){
	        	startGame(msg);
	        }else if(msg.chessCommand == "move"){
	        	if(msg.error != null){
	        		movePiece(msg.pieceID, msg.row + "|" + msg.col, false);
	        	}else{
	        		movePiece(msg.pieceID, msg.row + "|" + msg.col, true);
	        		if(msg.ml1 != null){
	        			tileFrom = document.getElementById(msg.ml1).title;
		        		tileTo = document.getElementById(msg.ml2).title;
		        		if(color == true)
			        		document.getElementById("moveList").innerHTML += "<span class='whiteMove'>" + tileFrom + " - " + tileTo + "</span><br>";
			        	else
			        		document.getElementById("moveList").innerHTML += "<span class='blackMove'>" + tileFrom + " - " + tileTo + "</span><br>";
			        	color = !color;
	        		}
	        	}
	        }else if(msg.chessCommand == "gameOver"){
	        	console.log(msg.winner + " has won the game!")
	        	document.getElementById("title").innerHTML = msg.winner + " has won the game!";
	        	document.getElementById("queueBtn").disabled = false;
	        }else if(msg.chessCommand == "gameReconnect"){
	        	var blackKing = document.getElementById("4");
	        	if(blackKing == null){
	        		initPieces();
	        	}
	        	document.getElementById("title").innerHTML = "Opponent: " + msg.opponent;
	        	document.getElementById("side").innerHTML = "You are " + msg.side;
	        	
	        	for(var i = 0; i < msg.numMoves; i++){
	        		var move = "move_" + i;
	        		tileFrom = document.getElementById(msg[move].id).parentElement.title;
	        		movePiece(msg[move].id, msg[move].row + "|" + msg[move].col, true);
	        		tileTo = document.getElementById(msg[move].id).parentElement.title;
	        		
	        		if(color == true)
		        		document.getElementById("moveList").innerHTML += "<span class='whiteMove'>" + tileFrom + " - " + tileTo + "</span><br>";
		        	else
		        		document.getElementById("moveList").innerHTML += "<span class='blackMove'>" + tileFrom + " - " + tileTo + "</span><br>";
		        	color = !color;
	        	}
	        }
	    });
		
		//For Chess AI matches
		stompClient.subscribe('/user/queue/chessPing', function (msg) {
			console.log(msg);
	        stompClient.send("/chess/chessPong", {}, "{pong:'pong'}");
	    });
		
		checkGameStatus();
	}, function(errorMessage){
		if(errorMessage.indexOf("Whoops!") !== -1){
			console.log("Attempting to reconnect...");
			setTimeout(connect, 3000);
		}
	});
}

function startGame(msgBody){
	clearInterval(timerVar);
	document.getElementById("title").innerHTML = "Opponent: " + msgBody.opponent;
	document.getElementById("side").innerHTML = "You are " + msgBody.side;
	
	chessGameStarted();
	initPieces();
}

function enterQueue(type, level){
	var json = new Object();
	json.chessCommand = "enterQueue";
	json.type = type;
	json.level = level;
	stompClient.send("/chess/chessMsg", {}, JSON.stringify(json));
}

function connect() {
	
	initStompChannels();
	
}
//an opponent was found and a game started
function chessGameStarted()
{
	document.getElementById("timer").innerHTML = "";
}

//Play the piece movement sound clip
function soundMove()
{
	if(!isMuted)
		audio.play();
}

function queueTimer()
{
	++timer;
	document.getElementById("timer").innerHTML = "Queue Time: "+ timer;
}


//Params: 
//-piece = the id of the img
//-tile  = the id of the chess board tile which holds the piece imgs
//***Use:***
//-This function is used when a move is received from the Web Socket connection
function movePiece(piece, tile, goodMove)
{
	var p = document.getElementById(piece);
	var t = document.getElementById(tile);
	
	//If the move is to take another piece, the piece being taken needs to be removed from the children
	if(t.children.length > 0 && t.children[0] != p)
	{
		console.log("Hiding piece");
		t.insertBefore(p, t.children[0]);
		t.children[1].style.display = "none";
	}
	else
	{
		t.appendChild(p);
	}	
	
	checkPawnPromotion(p);
	
	//soundMove();
}
function checkPawnPromotion(piece)
{
	var tile;
	
	if(parseInt(piece.id) > 15 && parseInt(piece.id) < 24)
	{
		tile = piece.parentNode;
		if(parseInt(tile.id.charAt(0)) == 0)
		{
			piece.src = "resources/images/white_queen.png";
		}
	}
	else if(parseInt(piece.id) > 7 && parseInt(piece.id) < 15)
	{
		tile = piece.parentNode;
		if(parseInt(tile.id.charAt(0)) == 7)
		{
			piece.src = "resources/images/black_queen.png";
		}
	}
}

//Send the player's move to the server for validation
function sendMove()
{
	//socket.send(moveFrom +"|"+ moveTo);
	var jsonMove = new Object();
	jsonMove.chessCommand = "move";
	jsonMove.pieceID = moveFrom;
	jsonMove.row = moveTo.split("|")[0];
	jsonMove.col = moveTo.split("|")[1];
	
	stompClient.send("/chess/chessMsg", {}, JSON.stringify(jsonMove));
}

//drag and drop event function
function allowDrop(ev) {
    ev.preventDefault();
    
}

//drag and drop event function
function drag(ev) {
    ev.dataTransfer.setData("text", ev.target.id);
    console.log("Dragging: " + ev.target.id);
    moveFrom = ev.target.id;
    tileFrom = ev.target.parentElement.title;
    test = ev.target.parentElement.title;
}

//drag and drop event function
//The problem with dropping onto an occupied tile is here - moveTo ends up being NULL
function drop(ev) {
    ev.preventDefault();
    var data = ev.dataTransfer.getData("text");
    console.log("The data dropped is: "+data);
    
    if(!isNaN(ev.target.id))
    {
    	console.log("orig ev data: " + ev.target.parentElement.id);
    	//ev.target = ev.target.parentElement;
    	console.log("parent ev data: " + ev.target.id);
    	moveTo = ev.target.parentElement.id;
    	
    	ev.target.parentElement.appendChild(document.getElementById(data));
    	//tileTo = ev.target.parentElement.title;
    }
    else
    {
    	 ev.target.appendChild(document.getElementById(data));
    	 moveTo = ev.target.id;
    	 //tileTo = ev.target.title;
    }

    //check if the ID of the element is a number (the IDs of the tiles are not truly numbers due to use of the pipe character)
    sendMove();
}

function createBoard()
{
	var board = document.getElementById("board");
	
	for(var i = 0; i < 8; i++)
	{
		var row = board.insertRow(i);
		
		
		for(var j = 0; j < 8; j++)
		{
			var cell = row.insertCell(j);
			var idStr = String.fromCharCode(97 + i).toUpperCase() + (j+1);
            cell.innerHTML = idStr;
            cell.id = idStr;
            cell.style.backgroundColor = 'blue';
            cell.style.color = 'white';
		}
	}
}

function initPieces()
{
	//<img src="images/black_pawn.png" id="8" draggable="true" ondragstart="drag(event)" style="max-width:100%; max-height:100%; display:block; margin:auto;">
	//Place black side PAWNS
	var col = 0;
	
	for(i = 0; i < 8; i++)
	{
		var x = document.createElement("IMG");
		x.id = "8";
		x.src = "resources/images/black_pawn.png";
		x.draggable = "true";
		x.ondragstart = "drag(event)";
		
		x.style.display = "block";
		x.style.maxHeight = "100%"
		x.style.maxWidth = "100%"
		x.style.margin = "auto auto auto auto";
		
		x.addEventListener('dragstart', drag, false);
		
		x.id = (parseInt(x.id) + i).toString();
		
		document.getElementById(1 + "|" + col++).appendChild(x);
	}
	
	col = 0;
	//Place white side PAWNS
	for(i = 0; i < 8; i++)
	{
		var x = document.createElement("IMG");
		x.id = "16";
		x.src = "resources/images/white_pawn.png";
		x.draggable = "true";
		x.ondragstart = "drag(event)";
		
		x.style.display = "block";
		x.style.maxHeight = "100%"
		x.style.maxWidth = "100%"
		x.style.margin = "auto";
		
		x.addEventListener('dragstart', drag, false);
		
		x.id = (parseInt(x.id) + i).toString();
		
		document.getElementById(6 + "|" + col++).appendChild(x);
	}
	
	//Place BLACK ROOKS
	var x = document.createElement("IMG");
	x.id = "0";
	x.src = "resources/images/black_rook.png";
	x.draggable = "true";
	x.ondragstart = "drag(event)";
	
	x.style.display = "block";
	x.style.maxHeight = "100%"
	x.style.maxWidth = "100%"
	x.style.margin = "auto";
	
	x.addEventListener('dragstart', drag, false);
	document.getElementById("0|0").appendChild(x);
	
	var x = document.createElement("IMG");
	x.id = "7";
	x.src = "resources/images/black_rook.png";
	x.draggable = "true";
	x.ondragstart = "drag(event)";
	
	x.style.display = "block";
	x.style.maxHeight = "100%"
	x.style.maxWidth = "100%"
	x.style.margin = "auto";
	
	x.addEventListener('dragstart', drag, false);
	document.getElementById("0|7").appendChild(x);
	
	
	//PLACE WHITE ROOKS
	var x = document.createElement("IMG");
	x.id = "24";
	x.src = "resources/images/white_rook.png";
	x.draggable = "true";
	x.ondragstart = "drag(event)";
	
	x.style.display = "block";
	x.style.maxHeight = "100%"
	x.style.maxWidth = "100%"
	x.style.margin = "auto";
	
	x.addEventListener('dragstart', drag, false);
	document.getElementById("7|0").appendChild(x);
	
	var x = document.createElement("IMG");
	x.id = "31";
	x.src = "resources/images/white_rook.png";
	x.draggable = "true";
	x.ondragstart = "drag(event)";
	
	x.style.display = "block";
	x.style.maxHeight = "100%"
	x.style.maxWidth = "100%"
	x.style.margin = "auto";
	
	x.addEventListener('dragstart', drag, false);
	document.getElementById("7|7").appendChild(x);
	
	//PLACE BLACK BISHOPS
	var x = document.createElement("IMG");
	x.id = "2";
	x.src = "resources/images/black_bishop.png";
	x.draggable = "true";
	x.ondragstart = "drag(event)";
	
	x.style.display = "block";
	x.style.maxHeight = "100%"
	x.style.maxWidth = "100%"
	x.style.margin = "auto";
	
	x.addEventListener('dragstart', drag, false);
	document.getElementById("0|2").appendChild(x);
	
	var x = document.createElement("IMG");
	x.id = "5";
	x.src = "resources/images/black_bishop.png";
	x.draggable = "true";
	x.ondragstart = "drag(event)";
	
	x.style.display = "block";
	x.style.maxHeight = "100%"
	x.style.maxWidth = "100%"
	x.style.margin = "auto";
	
	x.addEventListener('dragstart', drag, false);
	document.getElementById("0|5").appendChild(x);
	
	//PLACE WHITE BISHOPS
	var x = document.createElement("IMG");
	x.id = "26";
	x.src = "resources/images/white_bishop.png";
	x.draggable = "true";
	x.ondragstart = "drag(event)";
	
	x.style.display = "block";
	x.style.maxHeight = "100%"
	x.style.maxWidth = "100%"
	x.style.margin = "auto";
	
	x.addEventListener('dragstart', drag, false);
	document.getElementById("7|2").appendChild(x);
	
	var x = document.createElement("IMG");
	x.id = "29";
	x.src = "resources/images/white_bishop.png";
	x.draggable = "true";
	x.ondragstart = "drag(event)";
	
	x.style.display = "block";
	x.style.maxHeight = "100%"
	x.style.maxWidth = "100%"
	x.style.margin = "auto";
	
	x.addEventListener('dragstart', drag, false);
	document.getElementById("7|5").appendChild(x);
	
	//PLACE BLACK KNIGHTS	
	var x = document.createElement("IMG");
	x.id = "1";
	x.src = "resources/images/black_knight.png";
	x.draggable = "true";
	x.ondragstart = "drag(event)";
	
	x.style.display = "block";
	x.style.maxHeight = "100%"
	x.style.maxWidth = "100%"
	x.style.margin = "auto";
	
	x.addEventListener('dragstart', drag, false);
	document.getElementById("0|1").appendChild(x);
	
	var x = document.createElement("IMG");
	x.id = "6";
	x.src = "resources/images/black_knight.png";
	x.draggable = "true";
	x.ondragstart = "drag(event)";
	
	x.style.display = "block";
	x.style.maxHeight = "100%"
	x.style.maxWidth = "100%"
	x.style.margin = "auto";
	
	x.addEventListener('dragstart', drag, false);
	document.getElementById("0|6").appendChild(x);
	
	//PLACE WHITE KNIGHTS	
	var x = document.createElement("IMG");
	x.id = "25";
	x.src = "resources/images/white_knight.png";
	x.draggable = "true";
	x.ondragstart = "drag(event)";
	
	x.style.display = "block";
	x.style.maxHeight = "100%"
	x.style.maxWidth = "100%"
	x.style.margin = "auto";
	
	x.addEventListener('dragstart', drag, false);
	document.getElementById("7|1").appendChild(x);
	
	var x = document.createElement("IMG");
	x.id = "30";
	x.src = "resources/images/white_knight.png";
	x.draggable = "true";
	x.ondragstart = "drag(event)";
	
	x.style.display = "block";
	x.style.maxHeight = "100%"
	x.style.maxWidth = "100%"
	x.style.margin = "auto";
	
	x.addEventListener('dragstart', drag, false);
	document.getElementById("7|6").appendChild(x);
	
	//PLACE BLACK QUEEN
	var x = document.createElement("IMG");
	x.id = "3";
	x.src = "resources/images/black_queen.png";
	x.draggable = "true";
	x.ondragstart = "drag(event)";
	
	x.style.display = "block";
	x.style.maxHeight = "100%"
	x.style.maxWidth = "100%"
	x.style.margin = "auto";
	
	x.addEventListener('dragstart', drag, false);
	document.getElementById("0|3").appendChild(x);
	
	//PLACE WHITE QUEEN
	var x = document.createElement("IMG");
	x.id = "27";
	x.src = "resources/images/white_queen.png";
	x.draggable = "true";
	x.ondragstart = "drag(event)";
	
	x.style.display = "block";
	x.style.maxHeight = "100%"
	x.style.maxWidth = "100%"
	x.style.margin = "auto";
	
	x.addEventListener('dragstart', drag, false);
	document.getElementById("7|3").appendChild(x);
	
	//PLACE BLACK KING
	var x = document.createElement("IMG");
	x.id = "4";
	x.src = "resources/images/black_king.png";
	x.draggable = "true";
	x.ondragstart = "drag(event)";
	
	x.style.display = "block";
	x.style.maxHeight = "100%"
	x.style.maxWidth = "100%"
	x.style.margin = "auto";
	
	x.addEventListener('dragstart', drag, false);
	document.getElementById("0|4").appendChild(x);
	
	//PLACE WHITE KING
	var x = document.createElement("IMG");
	x.id = "28";
	x.src = "resources/images/white_king.png";
	x.draggable = "true";
	x.ondragstart = "drag(event)";
	
	x.style.display = "block";
	x.style.maxHeight = "100%"
	x.style.maxWidth = "100%"
	x.style.margin = "auto";
	
	x.addEventListener('dragstart', drag, false);
	document.getElementById("7|4").appendChild(x);
}

/*
 * Remove all the chess pieces from the board
 */
function removePieces()
{
	var i, j;
	
	for(i = 0; i < 8; i++)
	{
		for(j = 0; j < 8; j++)
		{
			var tileID = i + "|" + j;
			var tile = document.getElementById(tileID);
			
			while(tile.hasChildNodes())
			{
				tile.removeChild(tile.firstChild);
			}
			
		}
	}
}

function concede(player)
{
	var json = new Object();
	json.chessCommand = "concede";
	stompClient.send("/chess/chessMsg", {}, JSON.stringify(json));
}

/*
 * These functions relate to chess replays and their functionality
 */ 
function initReplay()
{
	prevPositions = [];
	var i;
	var prevTile;
	var piece;
	removePieces();
	initPieces();
	for(i = 0; i < replayMoveList.length; i++)
	{
		piece = JSON.parse(replayMoveList[i].move).pieceID;
		prevTile = document.getElementById(piece).parentElement.id;
		prevPositions[i] = piece + "|" + prevTile;
		moveForward();
	}
	buildReplayMoveList();
	
	removePieces();
	mlIndex = 0;
}

/*
 * get and parse the move list that was taken in from the server
 */
function setReplayMoveList(moveList)
{
	mlIndex = 0;
	replayMoveList = [];
	prevPositions = [];
	
	for(var i = 0; i < moveList.length; i++){
		replayMoveList[i] = moveList[i];
	}
	replayMoveList.pop();
	initReplay();
	initPieces();
	document.getElementById("replayBack").disabled = true;
}

/*
 * Used in chess replays - moves the game forward one move
 */
function moveForward()
{
	var piece, tile;
	piece = JSON.parse(replayMoveList[mlIndex].move).pieceID;
	tile = JSON.parse(replayMoveList[mlIndex].move).row +"|"+ JSON.parse(replayMoveList[mlIndex].move).col;	
	
	if(mlIndex < replayMoveList.length)
	{
		movePiece(piece, tile, true);
		mlIndex++;
	}
	replayButtonControl();
}

/*
 * used in chess replays - moves the game backwards one move
 */
function moveBackward()
{
	mlIndex--;
	var piece, tile;
	var parentEle;
	piece = prevPositions[mlIndex].split("|")[0];
	tile = prevPositions[mlIndex].split("|")[1] +"|"+ prevPositions[mlIndex].split("|")[2];
	
	//TODO: Find bug that happens when switching to different matches(a piece will skip moves and shift back to home position)
	parentEle = document.getElementById(piece).parentElement;
	
	movePiece(piece, tile, true);
	
	if(parentEle.hasChildNodes())
		parentEle.firstChild.style.display = "block";
	replayButtonControl();
}

/*
 * helper function that disables and enables the backwards and forwards buttons when they are unusable
 */
function replayButtonControl()
{
	console.log("replay btn control");
	if(replayMoveList.length == 0)
	{
		document.getElementById("replayBack").disabled = true;
		document.getElementById("replayForward").disabled = true;
		console.log("hit it");
		return;
	}
	
	if(mlIndex == 0)
	{
		document.getElementById("replayBack").disabled = true;
		document.getElementById("replayForward").disabled = false;
	}
	else if(mlIndex == replayMoveList.length && mlIndex != 0)
	{
		document.getElementById("replayForward").disabled = true;
		document.getElementById("replayBack").disabled = false;
	}
	else
	{
		document.getElementById("replayBack").disabled = false;
		document.getElementById("replayForward").disabled = false;
	}
}

//Make the move list from the selected replay
function buildReplayMoveList()
{
	document.getElementById("moveList").innerHTML = "";
	var c = true;
	var from, to, i;
	
	for(i = 0; i < replayMoveList.length; i++)
	{
		var fromID, toID;
		var moveJSON = JSON.parse(replayMoveList[i].move);
		
		fromID = prevPositions[i].substr(prevPositions[i].indexOf("|") + 1);
		toID = moveJSON.row + "|" + moveJSON.col;		
		
		from = document.getElementById(fromID).title;
		to = document.getElementById(toID).title;
		if(i % 2 == 0)
			document.getElementById("moveList").innerHTML += "<span class='whiteMove'>" +from + " - " + to + "</span><br>";
		else
			document.getElementById("moveList").innerHTML += "<span class='blackMove'>" +from + " - " + to + "</span><br>";
			
	}
}

//
function opponentSelect(val)
{
	if(val == "vsPlayer")
	{
		document.getElementById("queueBtn").style.display="inline";
		document.getElementById("concedeBtn").style.display="inline";
		
		if(document.getElementById("ReconnectBtn") != null)
			document.getElementById("ReconnectBtn").style.display="inline";
		
		document.getElementById("playAI").style.display="none";
		document.getElementById("compLevels").style.display = "none";
	}
	else
	{
		document.getElementById("queueBtn").style.display="none";
		document.getElementById("concedeBtn").style.display="inline";
		
		if(document.getElementById("ReconnectBtn") != null)
			document.getElementById("ReconnectBtn").style.display="none";
		
		document.getElementById("compLevels").style.display = "inline";
		document.getElementById("playAI").style.display="inline";
	}
}

function mute()
{
	isMuted = !isMuted;
	if(isMuted)
		document.getElementById("mute").innerHTML = "Unmute";
	else
		document.getElementById("mute").innerHTML = "Mute";
	
}



function stompWebSockets(){
	var socket = new SockJS('/ChessWeb/chessEndpoint');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame){
		stompClient.subscribe('/topic/msg', function (msg) {
	        alert(msg);
	    });
		
		stompClient.subscribe('/user/queue/privMsg', function (msg) {
	        alert(msg);
	        
	    });
		
		//For Chess messages
		stompClient.subscribe('/user/queue/chessMsg', function (msg) {
	        alert(msg);
	    });
		
		checkGameStatus();
	});
}

//Check if a game was in progress and reconnect to it if there was
function checkGameStatus(){
	var jsonMsg = new Object();
	jsonMsg.chessCommand = "reconnect";
	console.log(JSON.stringify(jsonMsg));
	
	stompClient.send("/chess/chessMsg", {}, JSON.stringify(jsonMsg));
}

function testMsg(){
	stompClient.send("/chess/message", {}, "{msg:From the client!}");
}

function testAI(){
	stompClient.send("/chess/testAI", {}, "{msg: inane message to trigger AI}");
}

function testMsgPriv(){
	stompClient.send("/chess/privMessage", {}, "{msg:From the client!}");
	
}