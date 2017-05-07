package com.thombs.ChessWeb.Models;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ChessGameDAOImpl implements ChessGameDAO{
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public ChessGame findGameByID(int id) {
		Session session = sessionFactory.getCurrentSession();
		ChessGame game = (ChessGame)session.get(ChessGame.class, id);
		return game;
	}

	@Override
	public ChessGame saveChessGame(ChessGame game) {
		Session session = sessionFactory.getCurrentSession();
		session.persist(game);
		return game;
	}

	@Override
	public List<ChessGame> getAllChessGames() {
		Session session = sessionFactory.getCurrentSession();
		List<ChessGame> games = session.createQuery("from ChessGame").getResultList();
		return games;
	}

	@Override
	public List<ChessGame> getChessGamesByUser(long userID) {
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createQuery("from ChessGame where playerWhite = :user or playerBlack = :user");
		q.setParameter("user", userID);
		List<ChessGame> games = q.getResultList();
		return games;
	}
	
}
