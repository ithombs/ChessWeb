package com.thombs.ChessWeb.Models.Chess;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


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

import com.thombs.ChessWeb.Controllers.ChessController;
import com.thombs.ChessWeb.DataAccess.Chess.ChessGameService;
import com.thombs.ChessWeb.DataAccess.User.UserService;
import com.thombs.ChessWeb.Models.Chess.ChessAI;
import com.thombs.ChessWeb.Models.Chess.ChessBoard;
import com.thombs.ChessWeb.Models.Chess.Json.ChessMoveMessage;
import com.thombs.ChessWeb.Models.User.User;


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
	private Map<String, Object> header;
	public ChessMatchmaking(){
		log.info("ChessMatchaker created! - " + this.hashCode());
		
		playerPool = new LinkedBlockingQueue<String>();
		activeGames = new HashMap<String, ChessBoard>();
		playerPingRecord = new HashMap<String, Long>();
		
		header = new HashMap<String, Object>();
		header.put("auto-delete", true);
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
		json.put("chessCommand", "GAME_START");
		json.put("opponent", "AI - " + level);
		json.put("side", aiGame.getPlayerTurn().equals(username)?"White":"Black");
		
		msgTemplate.convertAndSendToUser(username, ChessController.STOMP_GAME_START, json.toString(), header);
		
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
		moveJSONp1.put("chessCommand", "GAME_START");
		moveJSONp1.put("opponent", player1);
		moveJSONp1.put("side", game.getPlayerTurn().equals(player1)?"White":"Black");
		
		JSONObject moveJSONp2 = new JSONObject();
		moveJSONp2.put("chessCommand", "GAME_START");
		moveJSONp2.put("opponent", player2);
		moveJSONp2.put("side", game.getPlayerTurn().equals(player2)?"White":"Black");
		
		msgTemplate.convertAndSendToUser(player1, ChessController.STOMP_GAME_START, moveJSONp1.toString(),header);
		msgTemplate.convertAndSendToUser(player2, ChessController.STOMP_GAME_START, moveJSONp2.toString(),header);
		
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
		playerPool.forEach(player -> msgTemplate.convertAndSendToUser(player, "/queue/chessPing", "{'chessCommand':'ping'}",header));
		activeGames.forEach((player, game) -> msgTemplate.convertAndSendToUser(player, "/queue/chessPing", "{'chessCommand':'ping'}",header));
	}
	
	public void makeMove(String username, ChessMoveMessage msg){
		int pieceID = msg.getPieceID();
		int row = msg.getRow();
		int col = msg.getCol();
		
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
    				jsonMoveSuccess.put("chessCommand", "MOVE");
    				jsonMoveSuccess.put("pieceID", pieceID);
    				jsonMoveSuccess.put("row", row);
    				jsonMoveSuccess.put("col", col);
    				jsonMoveSuccess.put("ml1", previousPos.getRow()+ "|" + previousPos.getCol());
    				jsonMoveSuccess.put("ml2", row+ "|" + col);
				
				if(opponent.equals("AI")){
        			msgTemplate.convertAndSendToUser(username, ChessController.STOMP_MOVE, jsonMoveSuccess.toString(),header);
        			
        			if(currentGame.isGameOver())
        			{
        				JSONObject jsonGameOver = new JSONObject();
        				jsonGameOver.put("chessCommand", "GAME_OVER");
        				jsonGameOver.put("winner", username);
        				msgTemplate.convertAndSendToUser(username, ChessController.STOMP_GAME_OVER, jsonGameOver.toString(),header);
        				
        				currentGame.setWinner(username);
        				saveChessGame(currentGame);
        				activeGames.remove(username);
        			}else{
        				ChessAI ai = new ChessAI(currentGame.getAiLevel(), currentGame, msgTemplate, username, this);
        				ai.makeMove();
        			}
				}else{
					msgTemplate.convertAndSendToUser(username, ChessController.STOMP_MOVE, jsonMoveSuccess.toString(),header);
					msgTemplate.convertAndSendToUser(opponent, ChessController.STOMP_MOVE, jsonMoveSuccess.toString(),header);
					
					if(currentGame.isGameOver())
        			{
        				JSONObject jsonGameOver = new JSONObject();
        				jsonGameOver.put("chessCommand", "GAME_OVER");
        				jsonGameOver.put("winner", username);
        				msgTemplate.convertAndSendToUser(username, ChessController.STOMP_GAME_OVER, jsonGameOver.toString(),header);
        				msgTemplate.convertAndSendToUser(opponent, ChessController.STOMP_GAME_OVER, jsonGameOver.toString(),header);
        				
        				currentGame.setWinner(username);
        				saveChessGame(currentGame);
        				activeGames.remove(username);
        			}
				}
			}else{
				JSONObject badMove = new JSONObject();
				badMove.put("chessCommand", "MOVE");
				badMove.put("pieceID", previousPos.getID());
				badMove.put("row", previousPos.getRow());
				badMove.put("col", previousPos.getCol());
				badMove.put("error", "bad move");
				
				msgTemplate.convertAndSendToUser(username, ChessController.STOMP_MOVE, badMove.toString(),header);
			}
		}
	}
	
	public void playerReconnected(String username){
		ChessBoard gameInProgress = activeGames.get(username);
		if(gameInProgress != null){
			log.info("Game-in-progress found for [" + username +"] passing data to client");
			JSONObject gameData = new JSONObject();
			gameData.put("chessCommand", "RECONNECT");
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
			
			msgTemplate.convertAndSendToUser(username, ChessController.STOMP_RECONNECT, gameData.toString(),header);
		}else{
			log.info("No game-in-progress found for [" + username +"]");
		}
	}
	
	public void concede(String username){
		ChessBoard game = activeGames.get(username);
		String opponent;
		if(game != null){
			if(game.getPlayer1().equals(username)){
				opponent = game.getPlayer2();
			}else{
				opponent = game.getPlayer1();
			}
			game.setWinner(opponent);
			activeGames.remove(game.getPlayer1());
			activeGames.remove(game.getPlayer2());
			
			saveChessGame(game);
			
			JSONObject jsonGameOver = new JSONObject();
			jsonGameOver.put("chessCommand", "GAME_OVER");
			jsonGameOver.put("winner", opponent);
			msgTemplate.convertAndSendToUser(username, ChessController.STOMP_GAME_OVER, jsonGameOver.toString(),header);
			if(!opponent.equals("AI")){
				msgTemplate.convertAndSendToUser(opponent, ChessController.STOMP_GAME_OVER, jsonGameOver.toString(),header);
			}
			
			log.info("Chess game conceded by [" + username + "]");
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
