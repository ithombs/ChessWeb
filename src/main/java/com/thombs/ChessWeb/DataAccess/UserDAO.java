package com.thombs.ChessWeb.DataAccess;

import java.util.List;

import com.thombs.ChessWeb.Models.User;

public interface UserDAO{
	public User findByID(int id);
	public User findByUsername(String username);
	public User saveUser(User u);
	public List<User> getAllUsers();	
}
