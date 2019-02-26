package com.thombs.ChessWeb.Models.Chess.Json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChessEnterQueueMessage {
	@JsonProperty
	private ChessCommand chessCommand;
	@JsonProperty
	private int type;
	@JsonProperty
	private int level;
	
	public ChessEnterQueueMessage() {
	}
	
	public ChessCommand getChessCommand() {
		return chessCommand;
	}
	public void setChessCommand(ChessCommand chessCommand) {
		this.chessCommand = chessCommand;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
}
