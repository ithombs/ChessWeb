package com.thombs.ChessWeb.Models;

import java.sql.Timestamp;
import java.util.ArrayList;
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

import org.json.JSONObject;

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
	private long playerBlack;
	
	@Column(name = "playerwhite")
	private long playerWhite;
	
	@Column(name = "winner")
	private long winner;
	
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name = "chess_move_list", joinColumns = @JoinColumn(name="gameid"))
	private List<ChessMove> moves;
	
	public ChessGame(){
		
	}
	
	public ChessGame(ChessBoard game, long white, long black){
		this.setGameDate(new Timestamp(System.currentTimeMillis()));
		this.playerBlack = black;
		this.playerWhite = white;
		this.moves = new ArrayList<ChessMove>();
		
		int x = 1;
		for(ChessPiece piece : game.getMoveList()){
			ChessMove move = new ChessMove();
			move.setMoveNum(x);
			move.setMove(new JSONObject().put("chessCommand", "move").put("pieceID", piece.getID()).put("row", piece.getRow()).put("col", piece.getCol()).toString());
			moves.add(move);
			x++;
		}
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

	public long getPlayerBlack() {
		return playerBlack;
	}

	public void setPlayerBlack(long playerBlack) {
		this.playerBlack = playerBlack;
	}

	public long getPlayerWhite() {
		return playerWhite;
	}

	public void setPlayerWhite(long playerWhite) {
		this.playerWhite = playerWhite;
	}

	public long getWinner() {
		return winner;
	}

	public void setWinner(long winner) {
		this.winner = winner;
	}
	
	public String getGameDescription(long playerID){
		StringBuilder sb = new StringBuilder();
		if(playerID == winner){
			sb.append("W");
		}else{
			sb.append("L");
		}
		
		sb.append(" - ");
		sb.append(gameDate);
		
		return sb.toString();
	}
}
