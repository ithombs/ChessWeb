package com.thombs.ChessWeb.DataAccess;

import java.util.List;

import com.thombs.ChessWeb.Models.ChessGame;
import com.thombs.ChessWeb.Models.Leaderboard;

public interface ChessGameDAO {
	public ChessGame findGameByID(int id);
	public ChessGame saveChessGame(ChessGame game);
	public List<ChessGame> getAllChessGames();
	public List<ChessGame> getChessGamesByUser(long userID);
	public List<Leaderboard> getLeaderboard();
}
