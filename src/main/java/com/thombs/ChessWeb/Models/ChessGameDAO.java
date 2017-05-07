package com.thombs.ChessWeb.Models;

import java.util.List;

public interface ChessGameDAO {
	public ChessGame findGameByID(int id);
	public ChessGame saveChessGame(ChessGame game);
	public List<ChessGame> getAllChessGames();
	public List<ChessGame> getChessGamesByUser(long userID);
}
