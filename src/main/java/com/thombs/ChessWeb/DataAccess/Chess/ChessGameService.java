package com.thombs.ChessWeb.DataAccess.Chess;

import java.util.List;

import com.thombs.ChessWeb.Models.Leaderboard;
import com.thombs.ChessWeb.Models.Chess.ChessGame;

public interface ChessGameService {
	public ChessGame getChessGameByID(int id);
	public List<ChessGame> getAllChessGames();
	public ChessGame saveChessGame(ChessGame game);
	public List<ChessGame> getChessGamesByUser(long userID);
	public List<Leaderboard> getLeaderboard();
}
