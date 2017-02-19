package com.thombs.ChessWeb.Models;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

@Entity
@Table(name = "chess_game")
public class ChessGame {
	
	@Id
	@Column(name = "gameid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int gameID;

	@Column(name = "gamedate")
	private Timestamp gameDate;
	
	@Column(name = "playerblack")
	private int playerBlack;
	
	@Column(name = "playerwhite")
	private int playerWhite;
	
	@Column(name = "winner")
	private int winner;
	
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name = "chess_move_list", joinColumns = @JoinColumn(name="gameid"))
	private List<ChessMove> moves;
	
	public ChessGame(){
		
	}

	public int getGameID() {
		return gameID;
	}

	public List<ChessMove> getMoves() {
		return moves;
	}

	public void setMoves(List<ChessMove> moves) {
		this.moves = moves;
	}

	public void setGameID(int gameID) {
		this.gameID = gameID;
	}

	public Timestamp getGameDate() {
		return gameDate;
	}

	public void setGameDate(Timestamp gameDate) {
		this.gameDate = gameDate;
	}

	public int getPlayerBlack() {
		return playerBlack;
	}

	public void setPlayerBlack(int playerBlack) {
		this.playerBlack = playerBlack;
	}

	public int getPlayerWhite() {
		return playerWhite;
	}

	public void setPlayerWhite(int playerWhite) {
		this.playerWhite = playerWhite;
	}

	public int getWinner() {
		return winner;
	}

	public void setWinner(int winner) {
		this.winner = winner;
	}
	
	
}
