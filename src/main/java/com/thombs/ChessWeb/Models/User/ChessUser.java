package com.thombs.ChessWeb.Models.User;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class ChessUser extends org.springframework.security.core.userdetails.User{
	private static final long serialVersionUID = -6977840816481131717L;
	
	private User user;

	public ChessUser(User user, Collection<? extends GrantedAuthority> authorities){
		super(user.getUsername(), user.getPassword(), authorities);
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
