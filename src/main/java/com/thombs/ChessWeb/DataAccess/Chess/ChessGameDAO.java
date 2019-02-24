package com.thombs.ChessWeb.DataAccess.Chess;

import java.util.List;

import com.thombs.ChessWeb.Models.Leaderboard;
import com.thombs.ChessWeb.Models.Chess.ChessGame;

public interface ChessGameDAO {
	public ChessGame findGameByID(int id);
	public ChessGame saveChessGame(ChessGame game);
	public List<ChessGame> getAllChessGames();
	public List<ChessGame> getChessGamesByUser(long userID);
	public List<Leaderboard> getLeaderboard();
}
