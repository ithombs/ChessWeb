package com.thombs.ChessWeb.Models;

import java.util.List;


public interface UserService {
	public User getUser(int id);
	public User getUser(String username);
	public User saveUser(User u);
	public List<User> getAllUsers();
}
