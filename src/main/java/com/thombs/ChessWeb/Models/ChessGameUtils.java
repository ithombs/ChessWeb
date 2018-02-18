package com.thombs.ChessWeb.Models;

import java.util.List;

public class ChessGameUtils {
	public static int getNumWins(List<ChessGame> games, long userID){
		int numWins = 0;
		
		for(ChessGame game : games){
			if(game.getWinner() == userID){
				numWins++;
			}
		}
		
		return numWins;
	}
	
	public static int getNumBlackWins(List<ChessGame> games, long userID){
		int numWins = 0;
		
		for(ChessGame game : games){
			if(game.getWinner() == userID && game.getPlayerBlack() == userID){
				numWins++;
			}
		}
		
		return numWins;
	}
	
	public static int getNumWhiteWins(List<ChessGame> games, long userID){
		int numWins = 0;
		
		for(ChessGame game : games){
			if(game.getWinner() == userID && game.getPlayerWhite() == userID){
				numWins++;
			}
		}
		
		return numWins;
	}
}
