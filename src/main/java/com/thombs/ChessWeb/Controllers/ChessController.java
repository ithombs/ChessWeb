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

import com.thombs.ChessWeb.Models.ChessAI;
import com.thombs.ChessWeb.Models.ChessBoard;
import com.thombs.ChessWeb.Models.ChessMatchmaking;
import com.thombs.ChessWeb.Models.ChessPiece;
import com.thombs.ChessWeb.Models.Side;

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
			//username = headerAccessor.getSubscriptionId();
		}
		simp.convertAndSendToUser(username, "/queue/privMsg", "{msg: Private message from the server!}");
	}
	
	@MessageMapping("/chessPong")
	public void recievePong(SimpMessageHeaderAccessor headerAccessor){
		Principal user = headerAccessor.getUser();
		chessMM.recievePong(user.getName());
		//logger.info("Recieved ChessPong from [" + user.getName() +"]");
	}
	
	@MessageMapping("/chessMsg")
	public void chessMove(SimpMessageHeaderAccessor headerAccessor, String jsonMsg){
		Principal user = headerAccessor.getUser();
		//JSONObject json = new JSONObject();
		//json.put("chessCommand", "gameStart");
		//json.put("opponent", "dummyUser1");
		//json.put("side", "White");
		
		JSONObject recievedJSON = new JSONObject(jsonMsg);
		String commandType = recievedJSON.getString("chessCommand");
		switch(commandType){
			case "enterQueue":
				logger.info("ChessCommand [enterQueue] recieved from " + user.getName() + ": " + jsonMsg);
				if(recievedJSON.getString("type").equals("human")){
					chessMM.addPlayerToPool(user.getName());
				}else if(recievedJSON.getString("type").equals("AI")){
					chessMM.createAiGame(user.getName(), recievedJSON.getInt("level"));
				}
				break;
			case "move":
				logger.info("ChessCommand [move] recieved from " + user.getName() + ": " + jsonMsg);
				chessMM.makeMove(user.getName(), jsonMsg);
				break;
			case "reconnect":
				logger.info("ChessCommand [reconnect] recieved from " + user.getName() + ": " + jsonMsg);
				chessMM.playerReconnected(user.getName());
				break;
			default:
				logger.info("Unknown ChessCommand recieved from " + user.getName() + ": " + jsonMsg);
		}
		
		//logger.info(jsonMsg);
		//simp.convertAndSendToUser(user.getName(), "/queue/chessMsg", json.toString());
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
}
