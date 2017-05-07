package com.thombs.ChessWeb.Models;

import java.util.List;

public interface ChessGameService {
	public ChessGame getChessGameByID(int id);
	public List<ChessGame> getAllChessGames();
	public ChessGame saveChessGame(ChessGame game);
	public List<ChessGame> getChessGamesByUser(long userID);
}
