package com.thombs.ChessWeb.Models.Chess.Json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChessSimpleMessage {
	@JsonProperty
	private ChessCommand chessCommand;
	
	public ChessSimpleMessage() {
		
	}
	
	public ChessSimpleMessage(ChessCommand chessCommand) {
		this.chessCommand = chessCommand;
	}

	public ChessCommand getChessCommand() {
		return chessCommand;
	}

	public void setChessCommand(ChessCommand chessCommand) {
		this.chessCommand = chessCommand;
	}
}
