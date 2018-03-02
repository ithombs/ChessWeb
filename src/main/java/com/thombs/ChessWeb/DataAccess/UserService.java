package com.thombs.ChessWeb.DataAccess;

import java.util.List;

import com.thombs.ChessWeb.Models.User;


public interface UserService {
	public User getUser(int id);
	public User getUser(String username);
	public User saveUser(User u);
	public List<User> getAllUsers();
}
