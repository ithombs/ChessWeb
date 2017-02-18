package com.thombs.ChessWeb.Models;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

import javax.print.attribute.standard.DateTimeAtCompleted;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import com.thombs.ChessWeb.Models.ChessAI;
import com.thombs.ChessWeb.Models.ChessBoard;
import com.thombs.ChessWeb.Models.Side;
import com.thombs.ChessWeb.Models.ChessPiece;


@Service
@Scope(value = "singleton")
@EnableScheduling
public class ChessMatchmaking {
	private static final Logger log = LoggerFactory.getLogger(ChessMatchmaking.class);
	
	@Autowired
	private SimpMessagingTemplate msgTemplate;
	
	private Queue<String> playerPool;
	private Map<String, Long> playerPingRecord;
	private Map<String, ChessBoard> activeGames;
	private final String chessMsgDestination = "/queue/chessMsg";
	
	public ChessMatchmaking(){
		log.info("ChessMatchaker created! - " + this.hashCode());
		
		playerPool = new LinkedList<String>();
		activeGames = new HashMap<String, ChessBoard>();
		playerPingRecord = new HashMap<String, Long>();
		//games = new HashMap<String, ChessBoard>();
	}

	public void addPlayerToPool(String username){
		if(!playerPool.contains(username)){
			playerPool.add(username);
		}
		
		findMatch();
	}
	
	public void removePlayerFromPool(String username){
		playerPool.remove(username);
	}
	
	public void createAiGame(String username, int level){
		ChessBoard aiGame = new ChessBoard(username, level);
		activeGames.put(username, aiGame);
		
		JSONObject json = new JSONObject();
		json.put("chessCommand", "gameStart");
		json.put("opponent", "AI - " + level);
		json.put("side", aiGame.getPlayerTurn().equals(username)?"White":"Black");
		
		msgTemplate.convertAndSendToUser(username, chessMsgDestination, json.toString());
		
		if(aiGame.getPlayerTurn().equals("AI")){
			ChessAI ai = new ChessAI(aiGame.getAiLevel(), aiGame, msgTemplate, username);
		}
	}
	
	public boolean findMatch(){
    	//boolean foundMatch = false;
    	String player1, player2;
    	if(playerPool.size() > 1){
    		
    		player1 = playerPool.remove();
    		player2 = playerPool.remove();
    	}else{
    		return false;
    	}
		
		log.info("User: "  +player1 + " --- Opponent: " + player2);
		ChessBoard game = new ChessBoard(player1, player2);

		//Send to the client that they connected to a match
		JSONObject moveJSONp1 = new JSONObject();
		moveJSONp1.put("chessCommand", "gameStart");
		moveJSONp1.put("opponent", player1);
		moveJSONp1.put("side", game.getPlayerTurn().equals(player1)?"White":"Black");
		
		JSONObject moveJSONp2 = new JSONObject();
		moveJSONp2.put("chessCommand", "gameStart");
		moveJSONp2.put("opponent", player2);
		moveJSONp2.put("side", game.getPlayerTurn().equals(player2)?"White":"Black");
		
		msgTemplate.convertAndSendToUser(player1, chessMsgDestination, moveJSONp1.toString());
		msgTemplate.convertAndSendToUser(player2, chessMsgDestination, moveJSONp2.toString());
		
		activeGames.put(player1, game);
		activeGames.put(player2, game);
		return true;
	}
	
	public void recievePong(String username){
		long currentTime = System.currentTimeMillis();
		playerPingRecord.put(username, currentTime);
	}
	
	@Scheduled(fixedDelay = 10000)
	public void pingPlayers(){
		playerPool.forEach(player -> msgTemplate.convertAndSendToUser(player, "/queue/chessPing", "{'chessCommand':'ping'}"));
		activeGames.forEach((player, game) -> msgTemplate.convertAndSendToUser(player, "/queue/chessPing", "{'chessCommand':'ping'}"));
	}
	
	public void makeMove(String username, String jsonMove){
		JSONObject json = new JSONObject(jsonMove);
		int pieceID = json.getInt("pieceID");
		int row = json.getInt("row");
		int col = json.getInt("col");
		
		ChessBoard currentGame = activeGames.get(username);
		if(currentGame == null){
			msgTemplate.convertAndSendToUser(username, chessMsgDestination, new JSONObject().put("chessCommand", "error").put("msg", "No active game found").toString());
			return;
		}else{
			boolean correctSide = currentGame.getPlayerTurn().equals(username);
			ChessPiece previousPos = currentGame.getPiece(pieceID);
			
			String opponent;
			if(currentGame.getPlayer1().equals(username)){
				opponent = currentGame.getPlayer2();
			}else{
				opponent = currentGame.getPlayer1();
			}
			
			if(correctSide && currentGame.receiveMove(pieceID, row, col)){
				JSONObject jsonMoveSuccess = new JSONObject();
    			jsonMoveSuccess.put("chessCommand", "move");
    			jsonMoveSuccess.put("pieceID", pieceID);
    			jsonMoveSuccess.put("row", row);
    			jsonMoveSuccess.put("col", col);
    			jsonMoveSuccess.put("ml1", previousPos.getRow()+ "|" + previousPos.getCol());
    			jsonMoveSuccess.put("ml2", row+ "|" + col);
				
				if(opponent.equals("AI")){
        			msgTemplate.convertAndSendToUser(username, chessMsgDestination, jsonMoveSuccess.toString());
        			
        			if(currentGame.getPossibleMoves(Side.WHITE).size() == 0 || currentGame.getPossibleMoves(Side.BLACK).size() == 0)
        			{
        				JSONObject jsonGameOver = new JSONObject();
        				jsonGameOver.put("chessCommand", "gameOver");
        				jsonGameOver.put("winner", username);
        				msgTemplate.convertAndSendToUser(username, chessMsgDestination, jsonGameOver.toString());
        			}else{
        				ChessAI ai = new ChessAI(currentGame.getAiLevel(), currentGame, msgTemplate, username);
        			}
				}else{
					msgTemplate.convertAndSendToUser(username, chessMsgDestination, jsonMoveSuccess.toString());
					msgTemplate.convertAndSendToUser(opponent, chessMsgDestination, jsonMoveSuccess.toString());
					
					if(currentGame.getPossibleMoves(Side.WHITE).size() == 0 || currentGame.getPossibleMoves(Side.BLACK).size() == 0)
        			{
        				JSONObject jsonGameOver = new JSONObject();
        				jsonGameOver.put("chessCommand", "gameOver");
        				jsonGameOver.put("winner", username);
        				msgTemplate.convertAndSendToUser(username, chessMsgDestination, jsonGameOver.toString());
        				msgTemplate.convertAndSendToUser(opponent, chessMsgDestination, jsonGameOver.toString());
        			}
				}
			}else{
				JSONObject badMove = new JSONObject();
				badMove.put("chessCommand", "move");
				badMove.put("pieceID", pieceID);
				badMove.put("row", row);
				badMove.put("col", col);
				badMove.put("error", "bad move");
				
				msgTemplate.convertAndSendToUser(username, chessMsgDestination, badMove.toString());
			}
		}
	}
}
