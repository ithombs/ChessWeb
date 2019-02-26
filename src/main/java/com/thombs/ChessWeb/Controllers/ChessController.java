package com.thombs.ChessWeb.Controllers;

import java.security.Principal;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.thombs.ChessWeb.Models.Chess.ChessAI;
import com.thombs.ChessWeb.Models.Chess.ChessBoard;
import com.thombs.ChessWeb.Models.Chess.ChessMatchmaking;
import com.thombs.ChessWeb.Models.Chess.ChessPiece;
import com.thombs.ChessWeb.Models.Chess.Side;
import com.thombs.ChessWeb.Models.Chess.Json.ChessCommand;
import com.thombs.ChessWeb.Models.Chess.Json.ChessEnterQueueMessage;
import com.thombs.ChessWeb.Models.Chess.Json.ChessMoveMessage;
import com.thombs.ChessWeb.Models.Chess.Json.ChessSimpleMessage;

@Controller
public class ChessController {
	private static final Logger logger = LoggerFactory.getLogger(ChessController.class);
	
	@Autowired
    private SimpMessagingTemplate simp;
	
	@Autowired
	private ChessMatchmaking chessMM;
	
	//Client sends a message to the STOMP URL /chess/message
	@MessageMapping("/message")
	@SendTo("/topic/msg")
	public String sendMessage(){
		simp.getHeaderInitializer();
		return "From the server!";
	}
	
	@MessageMapping("/privMessage")
	public void sendPrivMessage(SimpMessageHeaderAccessor headerAccessor){
		Principal user = headerAccessor.getUser();
		String username;
		if(user != null){
			username = user.getName();
		}else{
			username = headerAccessor.getSessionId();
		}
		simp.convertAndSendToUser(username, "/queue/privMsg", "{msg: Private message from the server!}");
	}
	
	@MessageMapping("/chessPong")
	public void recievePong(SimpMessageHeaderAccessor headerAccessor){
		Principal user = headerAccessor.getUser();
		chessMM.recievePong(user.getName());
	}
	
	@MessageMapping("/chessMsg")
	public void chessMove(SimpMessageHeaderAccessor headerAccessor, String jsonMsg){
		Principal user = headerAccessor.getUser();
		
		JSONObject recievedJSON = new JSONObject(jsonMsg);
		String commandType = recievedJSON.getString("chessCommand");
		switch(commandType){
			default:
				logger.info("Unknown ChessCommand recieved from " + user.getName() + ": " + jsonMsg);
		}
	}
	
	@MessageMapping("/chessMsg-concede")
	public void concede(SimpMessageHeaderAccessor headerAccessor, ChessSimpleMessage msg){
		Principal user = headerAccessor.getUser();
		if(user != null) {
			logger.info("ChessCommand [concede] recieved from " + user.getName() + ": " + msg);
			chessMM.concede(user.getName());
		}else {
			logger.error("No user found!");
		}
	}
	
	@MessageMapping("/chessMsg-reconnect")
	public void reconnect(SimpMessageHeaderAccessor headerAccessor, ChessSimpleMessage msg){
		Principal user = headerAccessor.getUser();
		if(user != null) {
			logger.info("ChessCommand [reconnect] recieved from " + user.getName() + ": " + msg);
			chessMM.playerReconnected(user.getName());
		}else {
			logger.error("No user found!");
		}
	}
	
	@MessageMapping("/chessMsg-enterQueue")
	public void enterQueue(SimpMessageHeaderAccessor headerAccessor, ChessEnterQueueMessage msg){
		Principal user = headerAccessor.getUser();
		if(user != null) {
			logger.info("ChessCommand [enterQueue] recieved from " + user.getName() + ": " + msg);
			if(msg.getType() == 0){
				chessMM.addPlayerToPool(user.getName());
			}else if(msg.getType() == 1){
				chessMM.createAiGame(user.getName(), msg.getLevel());
			}
		}else {
			logger.error("No user found!");
		}
	}
	
	@MessageMapping("/chessMsg-move")
	public void chessMove(SimpMessageHeaderAccessor headerAccessor, ChessMoveMessage msg){
		Principal user = headerAccessor.getUser();
		if(user != null) {
			chessMM.makeMove(user.getName(), msg);	
		}else {
			logger.error("No user found!");
		}
	}
	
	@MessageMapping("/testAI")
	public void testAI(SimpMessageHeaderAccessor headerAccessor, String jsonMsg){
		ChessBoard testBoard = new ChessBoard();
		testBoard.setAiLevel(1);
		testBoard.setPlayer1("it12");
		testBoard.setPlayer2("AI");
		testBoard.setPlayerBlack("AI");
		testBoard.setPlayerWhite("it12");
		testBoard.setPlayerTurn("AI");
		testBoard.setTurn(Side.BLACK);
		
		ChessPiece[] pieces = testBoard.getBoard();
		for(ChessPiece piece : pieces){
			piece.setCaptured(true);
		}
		
		//Black king
		pieces[4].setCaptured(false);
		pieces[4].setRow(0);
		pieces[4].setCol(0);
		
		//Black pawns
		pieces[8].setCaptured(false);
		pieces[8].setRow(2);
		pieces[8].setCol(3);
		
		pieces[9].setCaptured(false);
		pieces[9].setRow(1);
		pieces[9].setCol(1);
		
		//White king
		pieces[28].setCaptured(false);
		pieces[28].setRow(7);
		pieces[28].setCol(7);
		
		//White pawns
		pieces[16].setCaptured(false);
		pieces[16].setRow(6);
		pieces[16].setCol(5);
		pieces[17].setCaptured(false);
		pieces[17].setRow(3);
		pieces[17].setCol(4);
		
		
		//Test AI move
		ChessAI ai = new ChessAI(testBoard.getAiLevel(), testBoard, simp, "it12", chessMM);
		ai.makeMove();
	}
	
	public static final String STOMP_MOVE = "/queue/chessMsg-move";
	public static final String STOMP_GAME_START = "/queue/chessMsg-gameStart";
	public static final String STOMP_GAME_OVER = "/queue/chessMsg-gameOver";
	public static final String STOMP_RECONNECT = "/queue/chessMsg-reconnect";
}
