package com.thombs.ChessWeb.Models;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAOImpl implements UserDAO{

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public User findByID(int id) {
		Session session = sessionFactory.getCurrentSession();
		User u = (User)session.get(User.class, id);
		return u;
	}

	@Override
	public User saveUser(User u) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(u);
		return u;
	}

	@Override
	public List<User> getAllUsers() {
		Session session = sessionFactory.getCurrentSession();
		List<User> users = session.createQuery("from User").getResultList();
		return users;
	}

	@Override
	public User findByUsername(String username) {
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createQuery("from User where username = :username");
		q.setString("username", username);
		User u = (User)q.uniqueResult();
		return u;
	}

}
