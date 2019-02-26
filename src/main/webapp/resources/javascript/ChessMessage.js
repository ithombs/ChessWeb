/**
 * This class is used for sending and receiving messages concerning a chess game (starting, ending, reconnecting, moves, etc).
 * The properties correlate directly to those found within ChessMessage.java
 */
const ChessCommand = {
    MOVE:"MOVE",
    GAME_START:"GAME_START",
    RECONNECT:"RECONNECT",
    CONCEDE:"CONCEDE",
    ENTER_QUEUE:"ENTER_QUEUE"
};

class ChessMoveMessage{
	constructor(chessCommand, pieceID, row, col, ml1, ml2){
		this.chessCommand = chessCommand;
		this.pieceID = pieceID;
		this.row = row;
		this.col = col;
		this.ml1 = ml1;
		this.ml2 = ml2;
	}
	
	toJson(){
		return JSON.stringify(this);
	}
	
	fromJson(msg){
		return new ChessMoveMessage(msg.chessCommand, msg.pieceID, msg.col, msg.ml1, msg.ml2);
	}
}

class ChessEnqueueMessage{
	constructor(chessCommand, type, level){
		this.chessCommand = chessCommand;
		this.type = type;
		this.level = level;
	}
	
	toJson(){
		return JSON.stringify(this);
	}
}

class ChessGameStartMessage{
	constructor(chessCommand, opponent, side){
		this.chessCommand = chessCommand;
		this.opponent = opponent;
		this.side = side;
	}
	
	toJson(){
		return JSON.stringify(this);
	}
}

class ChessConcedeMessage{
	constructor(chessCommand){
		this.chessCommand = chessCommand;
	}
	
	toJson(){
		return JSON.stringify(this);
	}
}