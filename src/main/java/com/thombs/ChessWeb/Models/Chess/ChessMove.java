package com.thombs.ChessWeb.Models.Chess;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;

@Embeddable
public class ChessMove {
	@Column(name = "movenumber")
	private int moveNum;
	
	@Column(name = "move")
	private String move;
	
	public ChessMove(){
		
	}

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
