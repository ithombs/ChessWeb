package com.thombs.ChessWeb.Models;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

import javax.print.attribute.standard.DateTimeAtCompleted;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;


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
	}
	
	public void removePlayerFromPool(String username){
		playerPool.remove(username);
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
		//TODO: Replace multiple send calls with one block of actual JSON data
		msgTemplate.convertAndSendToUser(player1, chessMsgDestination, "{'msg': 'connected'}");
		msgTemplate.convertAndSendToUser(player2, chessMsgDestination, "{'msg': 'connected'}");
		
		//Send opponent name and SIDE
		msgTemplate.convertAndSendToUser(player1, chessMsgDestination, "{'opponent': '" + player2 +"'}");
		msgTemplate.convertAndSendToUser(player2, chessMsgDestination, "{'opponent': '" + player1 +"'}");
		
		msgTemplate.convertAndSendToUser(player1, chessMsgDestination, "{'side': '" + (game.getPlayerTurn().equals(player1)?"white":"black") +"'}");
		msgTemplate.convertAndSendToUser(player2, chessMsgDestination, "{'side': '" + (game.getPlayerTurn().equals(player2)?"white":"black") +"'}");
		
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
		playerPool.forEach(player -> msgTemplate.convertAndSendToUser(player, "/queue/chessPing", "{'msg':'ping'}"));
		activeGames.forEach((player, game) -> msgTemplate.convertAndSendToUser(player, "/queue/chessPing", "{'msg':'ping'}"));
	}
}
