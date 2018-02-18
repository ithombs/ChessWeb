package com.thombs.ChessWeb.Models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;

@Embeddable
public class ChessMove {
	
	//@Column(name = "gameid")
	//private int gameID;
	
	@Column(name = "movenumber")
	private int moveNum;
	
	@Column(name = "move")
	private String move;
	
	public ChessMove(){
		
	}
	/*
	public int getGameID() {
		return gameID;
	}

	public void setGameID(int gameID) {
		this.gameID = gameID;
	}
	*/
	public int getMoveNum() {
		return moveNum;
	}

	public void setMoveNum(int moveNum) {
		this.moveNum = moveNum;
	}

	public String getMove() {
		return move;
	}

	public void setMove(String move) {
		this.move = move;
	}
}
