/**
 * Wrapper class for STOMP related chess messages and everything dealing with such
 */

class ChessStompSocket{
	socket;
	stompClient;
	
	
	constructor(){
		this.socket = new SockJS('/ChessWeb/chessEndpoint');
		this.initStompChannels = this.initStompChannels.bind(this);
		this.chessPingResponse = this.chessPingResponse.bind(this);
	}
	
	initStompConnection(){
		this.stompClient = Stomp.over(this.socket);
		this.stompClient.connect({}, this.initStompChannels);
	}
	
	initStompChannels(frame){
		console.log('Frame: ' + frame);
		
		this.stompClient.subscribe('/user/queue/chessMsg-gameStart', this.startGame,{"auto-delete": true});
		this.stompClient.subscribe('/user/queue/chessMsg-move', this.movePiece,{"auto-delete": true});
		this.stompClient.subscribe('/user/queue/chessMsg-gameOver', this.gameOver,{"auto-delete": true});
		this.stompClient.subscribe('/user/queue/chessMsg-reconnect', this.reconnect,{"auto-delete": true});
		
		this.stompClient.subscribe('/user/queue/chessMsg', function (msg) {
	        console.log('Message from /user/queue/chessMsg - ' + msg);
	    },{"auto-delete": true});
		
		//For Chess AI matches
		this.stompClient.subscribe('/user/queue/chessPing', this.chessPingResponse,{"auto-delete": true});
		
		this.checkGameStatus();
	}
	
	movePiece(msg){
		msg = JSON.parse(msg.body);
		var p = document.getElementById(msg.pieceID);
		var t = document.getElementById(msg.row + "|" + msg.col);
		
		//If the move is to take another piece, the piece being taken needs to be removed from the children
		if(t.children.length > 0 && t.children[0] != p)
		{
			t.insertBefore(p, t.children[0]);
			t.children[1].style.display = "none";
		}
		else
		{
			t.appendChild(p);
		}
		
		if(msg.error == null){
			if(msg.ml1 != null){
				tileFrom = document.getElementById(msg.ml1).title;
	    		tileTo = document.getElementById(msg.ml2).title;
	    		if(color == true){
	    			document.getElementById("moveList").innerHTML += "<span class='whiteMove'>" + tileFrom + " - " + tileTo + "</span><br>";
	    		}else{
	        		document.getElementById("moveList").innerHTML += "<span class='blackMove'>" + tileFrom + " - " + tileTo + "</span><br>";
	    		}
	        	color = !color;
			}
		}
		checkPawnPromotion(p);
		
		//soundMove();
	}
	
	startGame(msg){
		msg = JSON.parse(msg.body);
		clearInterval(timerVar);
		document.getElementById("title").innerHTML = "Opponent: " + msg.opponent;
		document.getElementById("side").innerHTML = "You are " + msg.side;
		
		chessGameStarted();
		initPieces();
	}
	
	gameOver(msg){
		msg = JSON.parse(msg.body);
		console.log(msg.winner + " has won the game!")
    	document.getElementById("title").innerHTML = msg.winner + " has won the game!";
    	document.getElementById("queueBtn").disabled = false;
	}
	
	reconnect(msg){
		msg = JSON.parse(msg.body);
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
    		
    		if(color == true){
    			document.getElementById("moveList").innerHTML += "<span class='whiteMove'>" + tileFrom + " - " + tileTo + "</span><br>";
    		}else{
    			document.getElementById("moveList").innerHTML += "<span class='blackMove'>" + tileFrom + " - " + tileTo + "</span><br>";
    		}
        	color = !color;
    	}
	}
	
	enterQueue(type, level){
		var queueMsg = new ChessEnqueueMessage(ChessCommand.ENTER_QUEUE, type, level);
		this.stompClient.send("/chess/chessMsg-enterQueue", {}, queueMsg.toJson());
	}
	
	sendMove(){
		var chessMove = new ChessMoveMessage(ChessCommand.MOVE, moveFrom, moveTo.split("|")[0], moveTo.split("|")[1]);
		this.stompClient.send("/chess/chessMsg-move", {}, chessMove.toJson());
	}
	
	concede(){
		var concedeMsg = new ChessConcedeMessage(ChessCommand.CONCEDE);
		this.stompClient.send("/chess/chessMsg-concede", {}, concedeMsg.toJson());
	}
	
	checkGameStatus(){
		var jsonMsg = new Object();
		jsonMsg.chessCommand = ChessCommand.RECONNECT;
		
		this.stompClient.send("/chess/chessMsg-reconnect", {}, JSON.stringify(jsonMsg));
	}
	
	chessPingResponse(msg){
		this.stompClient.send("/chess/chessPong", {}, "{pong:'pong'}");
	}
}