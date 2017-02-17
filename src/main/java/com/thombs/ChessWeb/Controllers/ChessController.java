package com.thombs.ChessWeb.Controllers;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.thombs.ChessWeb.Models.ChessMatchmaking;

@Controller
public class ChessController {
	private static final Logger logger = LoggerFactory.getLogger(ChessController.class);
	
	@Autowired
    SimpMessagingTemplate simp;
	
	@Autowired
	ChessMatchmaking chessMM;
	
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
	
	@MessageMapping("/chessMsg")
	public void chessMove(SimpMessageHeaderAccessor headerAccessor){
		Principal user = headerAccessor.getUser();
		
		
	}
}
