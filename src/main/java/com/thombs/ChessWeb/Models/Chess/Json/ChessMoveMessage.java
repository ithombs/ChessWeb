package com.thombs.ChessWeb.Models.Chess.Json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChessMoveMessage {
	@JsonProperty
	private ChessCommand chessCommand;
	@JsonProperty
	private int pieceID;
	@JsonProperty
	private int row;
	@JsonProperty
	private int col;
	
	@JsonProperty
	private String ml1;
	@JsonProperty
	private String ml2;
	
	public ChessMoveMessage() {
		
	}
	
	public ChessMoveMessage(int pieceID, int row, int col) {
		this.pieceID = pieceID;
		this.row = row;
		this.col = col;
	}
	
	public ChessMoveMessage(int pieceID, int row, int col, String ml1, String ml2) {
		this.pieceID = pieceID;
		this.row = row;
		this.col = col;
		this.ml1 = ml1;
		this.ml2 = ml2;
	}
	
	public ChessCommand getChessCommand() {
		return chessCommand;
	}
	public void setChessCommand(ChessCommand chessCommand) {
		this.chessCommand = chessCommand;
	}
	public int getPieceID() {
		return pieceID;
	}
	public void setPieceID(int pieceID) {
		this.pieceID = pieceID;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public String getMl1() {
		return ml1;
	}
	public void setMl1(String ml1) {
		this.ml1 = ml1;
	}
	public String getMl2() {
		return ml2;
	}
	public void setMl2(String ml2) {
		this.ml2 = ml2;
	}
}
