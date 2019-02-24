package com.thombs.ChessWeb.DataAccess.Chess;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thombs.ChessWeb.Models.Leaderboard;
import com.thombs.ChessWeb.Models.Chess.ChessGame;

@Service("ChessGameService")
public class ChessGameServiceImpl implements ChessGameService{

	@Autowired
	ChessGameDAO chessDAO;
	
	@Override
	@Transactional(readOnly = true)
	public ChessGame getChessGameByID(int id) {
		return chessDAO.findGameByID(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ChessGame> getAllChessGames() {
		return chessDAO.getAllChessGames();
	}

	@Override
	@Transactional
	public ChessGame saveChessGame(ChessGame game) {
		return chessDAO.saveChessGame(game);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ChessGame> getChessGamesByUser(long userID) {
		return chessDAO.getChessGamesByUser(userID);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Leaderboard> getLeaderboard() {
		return chessDAO.getLeaderboard();
	}
}
