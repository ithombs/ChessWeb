package com.thombs.ChessWeb.Models;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("ChessGameService")
public class ChessGameServiceImpl implements ChessGameService{

	@Autowired
	ChessGameDAO chessDAO;
	
	@Override
	@Transactional
	public ChessGame getChessGameByID(int id) {
		return chessDAO.findGameByID(id);
	}

	@Override
	@Transactional
	public List<ChessGame> getAllChessGames() {
		return chessDAO.getAllChessGames();
	}

	@Override
	@Transactional
	public ChessGame saveChessGame(ChessGame game) {
		return chessDAO.saveChessGame(game);
	}

	@Override
	@Transactional
	public List<ChessGame> getChessGamesByUser(int userID) {
		return chessDAO.getChessGamesByUser(userID);
	}

}
