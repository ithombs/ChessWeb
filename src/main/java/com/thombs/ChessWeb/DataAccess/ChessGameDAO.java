package com.thombs.ChessWeb.DataAccess;

import java.util.List;

import com.thombs.ChessWeb.Models.ChessGame;

public interface ChessGameDAO {
	public ChessGame findGameByID(int id);
	public ChessGame saveChessGame(ChessGame game);
	public List<ChessGame> getAllChessGames();
	public List<ChessGame> getChessGamesByUser(long userID);
}
