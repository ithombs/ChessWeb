package com.thombs.ChessWeb.Models;

import java.util.List;



public interface UserDAO{
	public User findByID(int id);
	public User findByUsername(String username);
	public User saveUser(User u);
	public List<User> getAllUsers();
	
}
