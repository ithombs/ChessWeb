package com.thombs.ChessWeb.Models;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.print.attribute.standard.DateTimeAtCompleted;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
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


@Configuration
//@EnableSpringConfigured
@Service
@Scope(value = "singleton")
@EnableScheduling
public class ChessMatchmaking {
	private static final Logger log = LoggerFactory.getLogger(ChessMatchmaking.class);
	
	@Autowired
	private SimpMessagingTemplate msgTemplate;
	
	@Autowired
	private ChessGameService chessService;
	
	@Autowired
	private UserService userService;
	
	private BlockingQueue<String> playerPool;
	private Map<String, Long> playerPingRecord;
	private Map<String, ChessBoard> activeGames;
	private final String chessMsgDestination = "/queue/chessMsg";
	
	public ChessMatchmaking(){
		log.info("ChessMatchaker created! - " + this.hashCode());
		
		playerPool = new LinkedBlockingQueue<String>();
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
			ChessAI ai = new ChessAI(aiGame.getAiLevel(), aiGame, msgTemplate, username, this);
			ai.makeMove();
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
        			
        			if(currentGame.isGameOver())
        			{
        				JSONObject jsonGameOver = new JSONObject();
        				jsonGameOver.put("chessCommand", "gameOver");
        				jsonGameOver.put("winner", username);
        				msgTemplate.convertAndSendToUser(username, chessMsgDestination, jsonGameOver.toString());
        				
        				currentGame.setWinner(username);
        				saveChessGame(currentGame);
        				activeGames.remove(username);
        			}else{
        				ChessAI ai = new ChessAI(currentGame.getAiLevel(), currentGame, msgTemplate, username, this);
        				ai.makeMove();
        			}
				}else{
					msgTemplate.convertAndSendToUser(username, chessMsgDestination, jsonMoveSuccess.toString());
					msgTemplate.convertAndSendToUser(opponent, chessMsgDestination, jsonMoveSuccess.toString());
					
					if(currentGame.isGameOver())
        			{
        				JSONObject jsonGameOver = new JSONObject();
        				jsonGameOver.put("chessCommand", "gameOver");
        				jsonGameOver.put("winner", username);
        				msgTemplate.convertAndSendToUser(username, chessMsgDestination, jsonGameOver.toString());
        				msgTemplate.convertAndSendToUser(opponent, chessMsgDestination, jsonGameOver.toString());
        				
        				currentGame.setWinner(username);
        				saveChessGame(currentGame);
        				activeGames.remove(username);
        			}
				}
			}else{
				JSONObject badMove = new JSONObject();
				badMove.put("chessCommand", "move");
				badMove.put("pieceID", previousPos.getID());
				badMove.put("row", previousPos.getRow());
				badMove.put("col", previousPos.getCol());
				badMove.put("error", "bad move");
				
				msgTemplate.convertAndSendToUser(username, chessMsgDestination, badMove.toString());
			}
		}
	}
	
	public void playerReconnected(String username){
		ChessBoard gameInProgress = activeGames.get(username);
		if(gameInProgress != null){
			log.info("Game-in-progress found for [" + username +"] passing data to client");
			JSONObject gameData = new JSONObject();
			gameData.put("chessCommand", "gameReconnect");
			gameData.put("numMoves", gameInProgress.getMoveList().size());
			gameData.put("side", gameInProgress.getPlayerWhite().equals(username)?"White":"Black");
			gameData.put("opponent", gameInProgress.getPlayer1().equals(username)?gameInProgress.getPlayer2():gameInProgress.getPlayer1());
			
			for(int i = 0; i < gameInProgress.getMoveList().size(); i++){
				JSONObject moveJSON = new JSONObject();
				moveJSON.put("id", gameInProgress.getMoveList().get(i).getID());
				moveJSON.put("row", gameInProgress.getMoveList().get(i).getRow());
				moveJSON.put("col", gameInProgress.getMoveList().get(i).getCol());
				gameData.put("move_"+i, moveJSON);
			}
			
			msgTemplate.convertAndSendToUser(username, chessMsgDestination, gameData.toString());
			
		}else{
			log.info("No game-in-progress found for [" + username +"]");
		}
	}
	
	//Don't think I like this. Probably a better way to save games from here
	public void saveChessGame(ChessBoard game){
		ChessGame chessGame;
		User white, black;
		long winner;
		
		if(!game.getPlayerWhite().equals("AI")){
			white = userService.getUser(game.getPlayerWhite());
		}else{
			white = new User();
			white.setUserid(-1);
			white.setUsername("AI");
		}
		if(!game.getPlayerBlack().equals("AI")){
			black = userService.getUser(game.getPlayerBlack());
		}else{
			black = new User();
			black.setUserid(-1);
			black.setUsername("AI");
		}
		
		if(game.getWinner().equals(white.getUsername())){
			winner = white.getUserid();
		}else{
			winner = black.getUserid();
		}
		
		chessGame = new ChessGame(game, white.getUserid(), black.getUserid());
		chessGame.setWinner(winner);
		
		chessService.saveChessGame(chessGame);
	}
}
