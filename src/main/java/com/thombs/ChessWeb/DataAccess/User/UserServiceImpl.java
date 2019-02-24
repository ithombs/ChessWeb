package com.thombs.ChessWeb.DataAccess.User;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thombs.ChessWeb.Aspect.LoggerTest;
import com.thombs.ChessWeb.Models.User.User;

@Service("UserService")
public class UserServiceImpl implements UserService{

	@Autowired
	UserDAO userDAO;
	
	@Autowired
	PasswordEncoder passEncrypt;

	@Override
	@Transactional
	public User saveUser(User u) {
		u.setPassword(passEncrypt.encode(u.getPassword()));
		u.setEmail("Not used");
		return userDAO.saveUser(u);
	}

	@Override
	@Transactional
	@LoggerTest(level = 0)
	public User getUser(int id) {
		User u = userDAO.findByID(id);
		return u;
	}

	@Override
	@Transactional
	public List<User> getAllUsers() {
		List<User> users = userDAO.getAllUsers();
		return users;
	}

	@Override
	@Transactional
	@LoggerTest(level = 1, activityName = "Retrived User")
	public User getUser(String username) {
		User u = userDAO.findByUsername(username);
		return u;
	}
}
